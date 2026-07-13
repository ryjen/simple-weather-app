package com.example.myweatherapp.features.forecast.types

import com.example.myweatherapp.domain.model.ForecastResponse
import com.example.myweatherapp.domain.model.GeocodingResponse

data class ForecastState(
    val cityName: String = "",
    val searchResults: List<GeocodingResponse> = emptyList(),
    val forecast: ForecastResponse? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface ForecastIntent {
    data class ForecastDataChanged(
        val forecast: ForecastResponse?,
        val isLoading: Boolean
    ) : ForecastIntent

    data object BackClicked : ForecastIntent
    data class CityNameChanged(val value: String) : ForecastIntent
    data object SearchSubmitted : ForecastIntent
    data class SearchResultsFound(val results: List<GeocodingResponse>) : ForecastIntent
    data class CitySelected(val location: GeocodingResponse) : ForecastIntent
    data class SearchFailed(val message: String) : ForecastIntent
    data object SearchStarted : ForecastIntent
}

sealed interface ForecastEffect {
    data object NavigateBack : ForecastEffect
    data object NavigateToWeather : ForecastEffect
}
