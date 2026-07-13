package com.example.myweatherapp.features.weather.interactors

import androidx.lifecycle.viewModelScope
import com.example.myweatherapp.core.mvi.Interactor
import com.example.myweatherapp.features.weather.reducers.weatherReducer
import com.example.myweatherapp.features.weather.types.WeatherEffect
import com.example.myweatherapp.features.weather.types.WeatherIntent
import com.example.myweatherapp.features.weather.types.WeatherState
import com.example.myweatherapp.features.weather.usecases.LoadLastCityUseCase
import com.example.myweatherapp.features.weather.usecases.SearchWeatherUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * The Interactor responsible for managing the business logic of the Weather feature.
 * It handles the MVI flow by processing [WeatherIntent]s and emitting [WeatherState] updates
 * and [WeatherEffect]s.
 *
 * @property searchWeatherUseCase Use case for searching weather by city name.
 * @property loadLastCityUseCase Use case for retrieving the previously searched city.
 */
class WeatherInteractor(
    private val searchWeatherUseCase: SearchWeatherUseCase,
    private val loadLastCityUseCase: LoadLastCityUseCase
) : Interactor<WeatherState, WeatherIntent, WeatherEffect>(WeatherState(), ::weatherReducer) {

    private var searchJob: Job? = null

    init {
        // Automatically load the last searched city on initialization
        dispatch(WeatherIntent.LoadLastCityRequested)
    }

    /**
     * Processes side-effect triggering intents.
     */
    override fun induce(intent: WeatherIntent, state: WeatherState) {
        when (intent) {
            WeatherIntent.LoadLastCityRequested -> loadLastCity()
            WeatherIntent.SearchSubmitted,
            WeatherIntent.RetryRequested -> search(state.cityName)

            is WeatherIntent.CitySelected -> fetchWeather(intent.location)
            WeatherIntent.ForecastClicked -> emit(WeatherEffect.NavigateToForecast)
            is WeatherIntent.CityNameChanged,
            is WeatherIntent.LastCityLoaded,
            is WeatherIntent.SearchFailed,
            is WeatherIntent.SearchStarted,
            is WeatherIntent.SearchSucceeded,
            is WeatherIntent.SearchResultsFound -> Unit
        }
    }

    /**
     * Attempts to load the last city and triggers a search if successful.
     */
    private fun loadLastCity() {
        viewModelScope.launch {
            val cityName = loadLastCityUseCase.execute()?.trim().orEmpty()
            if (cityName.isNotEmpty()) {
                dispatch(WeatherIntent.LastCityLoaded(cityName))
                // For last city, we just search and take the first result as before, 
                // but we use the existing execute(cityName) which does exactly that.
                search(cityName, isInitialLoad = true)
            }
        }
    }

    /**
     * Executes the search operation for the given city name.
     * Updates the state with loading, success, or failure results.
     */
    private fun search(rawCityName: String, isInitialLoad: Boolean = false) {
        val cityName = rawCityName.trim()
        if (cityName.isEmpty()) return

        searchJob?.cancel()
        dispatch(WeatherIntent.SearchStarted(cityName))
        searchJob = viewModelScope.launch {
            runCatching {
                if (isInitialLoad) {
                    searchWeatherUseCase.execute(cityName)
                } else {
                    val results = searchWeatherUseCase.searchCities(cityName)
                    if (results.isEmpty()) {
                        error("City not found")
                    } else if (results.size == 1) {
                        searchWeatherUseCase.fetchWeatherForLocation(results.first())
                    } else {
                        // More than one result found, let user select
                        dispatch(WeatherIntent.SearchResultsFound(results))
                        return@launch
                    }
                }
            }
                .onSuccess { result ->
                    if (result is com.example.myweatherapp.features.weather.usecases.SearchWeatherResult) {
                        val weather = result.currentWeather
                        val fullName = "${weather.name}" // Or use result location if we had it
                        dispatch(
                            WeatherIntent.SearchSucceeded(
                                cityName = fullName,
                                currentWeather = result.currentWeather,
                                forecast = result.forecast
                            )
                        )
                    }
                }
                .onFailure { error ->
                    dispatch(
                        WeatherIntent.SearchFailed(
                            error.message ?: "An unknown error occurred"
                        )
                    )
                }
        }
    }

    private fun fetchWeather(location: com.example.myweatherapp.domain.model.GeocodingResponse) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            runCatching { searchWeatherUseCase.fetchWeatherForLocation(location) }
                .onSuccess { result ->
                    val fullName =
                        "${location.name}${location.state?.let { ", $it" } ?: ""}, ${location.country}"
                    dispatch(
                        WeatherIntent.SearchSucceeded(
                            cityName = fullName,
                            currentWeather = result.currentWeather,
                            forecast = result.forecast
                        )
                    )
                }
                .onFailure { error ->
                    dispatch(
                        WeatherIntent.SearchFailed(
                            error.message ?: "An unknown error occurred"
                        )
                    )
                }
        }
    }
}
