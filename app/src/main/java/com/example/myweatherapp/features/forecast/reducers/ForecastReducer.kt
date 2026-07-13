package com.example.myweatherapp.features.forecast.reducers

import com.example.myweatherapp.features.forecast.types.ForecastIntent
import com.example.myweatherapp.features.forecast.types.ForecastState
import com.example.myweatherapp.features.forecast.types.toDailyForecasts

fun forecastReducer(state: ForecastState, intent: ForecastIntent): ForecastState {
    return when (intent) {
        is ForecastIntent.ForecastDataChanged -> state.copy(
            days = intent.forecast?.toDailyForecasts().orEmpty(),
            isLoading = intent.isLoading
        )
        ForecastIntent.BackClicked -> state
    }
}
