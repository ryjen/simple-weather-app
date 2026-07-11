package com.example.myweatherapp.features.weather.reducers

import com.example.myweatherapp.features.weather.types.WeatherIntent
import com.example.myweatherapp.features.weather.types.WeatherState

fun weatherReducer(state: WeatherState, intent: WeatherIntent): WeatherState {
    return when (intent) {
        is WeatherIntent.CityNameChanged -> state.copy(cityName = intent.value)
        is WeatherIntent.LastCityLoaded -> state.copy(cityName = intent.cityName)
        is WeatherIntent.SearchStarted -> state.copy(
            cityName = intent.cityName,
            isLoading = true,
            errorMessage = null
        )
        is WeatherIntent.SearchSucceeded -> state.copy(
            cityName = intent.cityName,
            currentWeather = intent.currentWeather,
            forecast = intent.forecast,
            isLoading = false,
            errorMessage = null
        )
        is WeatherIntent.SearchFailed -> state.copy(
            isLoading = false,
            errorMessage = intent.message
        )
        WeatherIntent.ForecastClicked,
        WeatherIntent.LoadLastCityRequested,
        WeatherIntent.RetryRequested,
        WeatherIntent.SearchSubmitted -> state
    }
}
