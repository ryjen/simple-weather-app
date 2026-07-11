package com.example.myweatherapp.features.forecast.types

import com.example.myweatherapp.data.model.ForecastResponse

data class ForecastState(
    val forecast: ForecastResponse? = null,
    val isLoading: Boolean = false
)

sealed interface ForecastIntent {
    data class ForecastDataChanged(
        val forecast: ForecastResponse?,
        val isLoading: Boolean
    ) : ForecastIntent

    data object BackClicked : ForecastIntent
}

sealed interface ForecastEffect {
    data object NavigateBack : ForecastEffect
}
