package com.example.myweatherapp.domain.types

import com.example.myweatherapp.domain.model.ForecastResponse

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
