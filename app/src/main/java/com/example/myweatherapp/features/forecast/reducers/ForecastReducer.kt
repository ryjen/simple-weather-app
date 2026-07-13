package com.example.myweatherapp.features.forecast.reducers

import com.example.myweatherapp.features.forecast.types.ForecastIntent
import com.example.myweatherapp.features.forecast.types.ForecastState

fun forecastReducer(state: ForecastState, intent: ForecastIntent): ForecastState {
    return when (intent) {
        is ForecastIntent.ForecastDataChanged -> state.copy(
            forecast = intent.forecast,
            isLoading = intent.isLoading,
            searchResults = emptyList(),
            errorMessage = null
        )

        ForecastIntent.BackClicked -> state
        is ForecastIntent.CityNameChanged -> state.copy(cityName = intent.value)
        ForecastIntent.SearchStarted -> state.copy(
            isLoading = true,
            errorMessage = null,
            searchResults = emptyList()
        )

        is ForecastIntent.SearchResultsFound -> state.copy(
            isLoading = false,
            searchResults = intent.results
        )

        is ForecastIntent.SearchFailed -> state.copy(
            isLoading = false,
            errorMessage = intent.message
        )

        is ForecastIntent.CitySelected -> state.copy(isLoading = true, searchResults = emptyList())
        ForecastIntent.SearchSubmitted -> state
    }
}
