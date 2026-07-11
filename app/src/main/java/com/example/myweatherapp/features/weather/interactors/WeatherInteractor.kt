package com.example.myweatherapp.features.weather.interactors

import androidx.lifecycle.viewModelScope
import com.example.myweatherapp.core.mvi.Interactor
import com.example.myweatherapp.features.weather.reducers.weatherReducer
import com.example.myweatherapp.features.weather.types.WeatherEffect
import com.example.myweatherapp.features.weather.types.WeatherIntent
import com.example.myweatherapp.features.weather.types.WeatherState
import com.example.myweatherapp.features.weather.usecases.LoadLastCityUseCase
import com.example.myweatherapp.features.weather.usecases.SearchWeatherUseCase
import kotlinx.coroutines.launch

class WeatherInteractor(
    private val searchWeatherUseCase: SearchWeatherUseCase,
    private val loadLastCityUseCase: LoadLastCityUseCase
) : Interactor<WeatherState, WeatherIntent, WeatherEffect>(WeatherState(), ::weatherReducer) {

    init {
        dispatch(WeatherIntent.LoadLastCityRequested)
    }

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

    private fun loadLastCity() {
        viewModelScope.launch {
            val cityName = loadLastCityUseCase.execute()?.trim().orEmpty()
            if (cityName.isNotEmpty()) {
                dispatch(WeatherIntent.LastCityLoaded(cityName))
                dispatch(WeatherIntent.SearchSubmitted)
            }
        }
    }

    private fun search(rawCityName: String) {
        val cityName = rawCityName.trim()
        if (cityName.isEmpty()) return

        dispatch(WeatherIntent.SearchStarted(cityName))
        viewModelScope.launch {
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
