package com.example.myweatherapp.domain.reducer

import com.example.myweatherapp.domain.types.ForecastIntent
import com.example.myweatherapp.domain.types.ForecastState

fun forecastReducer(state: ForecastState, intent: ForecastIntent): ForecastState {
    return when (intent) {
        is ForecastIntent.ForecastDataChanged -> state.copy(
            forecast = intent.forecast,
            isLoading = intent.isLoading
        )

        ForecastIntent.BackClicked -> state
    }
}
