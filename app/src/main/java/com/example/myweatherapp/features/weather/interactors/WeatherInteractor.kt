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
            WeatherIntent.ForecastClicked -> emit(WeatherEffect.NavigateToForecast)
            is WeatherIntent.CityNameChanged,
            is WeatherIntent.LastCityLoaded,
            is WeatherIntent.SearchFailed,
            is WeatherIntent.SearchStarted,
            is WeatherIntent.SearchSucceeded -> Unit
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
                dispatch(WeatherIntent.SearchSubmitted)
            }
        }
    }

    /**
     * Executes the search operation for the given city name.
     * Updates the state with loading, success, or failure results.
     */
    private fun search(rawCityName: String) {
        val cityName = rawCityName.trim()
        if (cityName.isEmpty()) return

        searchJob?.cancel()
        dispatch(WeatherIntent.SearchStarted(cityName))
        searchJob = viewModelScope.launch {
            runCatching { searchWeatherUseCase.execute(cityName) }
                .onSuccess { result ->
                    dispatch(
                        WeatherIntent.SearchSucceeded(
                            cityName = cityName,
                            currentWeather = result.currentWeather,
                            forecast = result.forecast
                        )
                    )
                }
                .onFailure { error ->
                    dispatch(WeatherIntent.SearchFailed(error.message ?: "An unknown error occurred"))
                }
        }
    }
}
