package com.example.myweatherapp.features.forecast.reducers

import com.example.myweatherapp.features.forecast.types.ForecastIntent
import com.example.myweatherapp.features.forecast.types.ForecastState

fun forecastReducer(state: ForecastState, intent: ForecastIntent): ForecastState {
    return when (intent) {
        is ForecastIntent.ForecastDataChanged -> state.copy(
            forecast = intent.forecast,
            isLoading = intent.isLoading
        )
        ForecastIntent.BackClicked -> state
    }
}
