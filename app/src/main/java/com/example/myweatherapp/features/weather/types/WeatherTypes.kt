package com.example.myweatherapp.features.weather.types

import com.example.myweatherapp.domain.model.CurrentWeatherResponse
import com.example.myweatherapp.domain.model.ForecastResponse
import com.example.myweatherapp.domain.model.GeocodingResponse

data class WeatherState(
    val cityName: String = "",
    val searchResults: List<GeocodingResponse> = emptyList(),
    val currentWeather: CurrentWeatherResponse? = null,
    val forecast: ForecastResponse? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface WeatherIntent {
    data class CityNameChanged(val value: String) : WeatherIntent
    data object LoadLastCityRequested : WeatherIntent
    data class LastCityLoaded(val cityName: String) : WeatherIntent
    data object SearchSubmitted : WeatherIntent
    data object RetryRequested : WeatherIntent
    data class SearchStarted(val cityName: String) : WeatherIntent
    data class SearchSucceeded(
        val cityName: String,
        val currentWeather: CurrentWeatherResponse,
        val forecast: ForecastResponse
    ) : WeatherIntent

    data class SearchFailed(val message: String) : WeatherIntent
    data object ForecastClicked : WeatherIntent
    data class SearchResultsFound(val results: List<GeocodingResponse>) : WeatherIntent
    data class CitySelected(val location: GeocodingResponse) : WeatherIntent
}

sealed interface WeatherEffect {
    data object NavigateToForecast : WeatherEffect
}
