package com.example.myweatherapp.features.weather.usecases

import com.example.myweatherapp.domain.model.CurrentWeatherResponse
import com.example.myweatherapp.domain.model.ForecastResponse
import com.example.myweatherapp.domain.model.GeocodingResponse
import com.example.myweatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

data class SearchWeatherResult(
    val currentWeather: CurrentWeatherResponse,
    val forecast: ForecastResponse
)

class SearchWeatherUseCase(
    private val repository: WeatherRepository
) {
    suspend fun execute(cityName: String): SearchWeatherResult = coroutineScope {
        val coords = repository.getCoordinates(cityName)
        val location = coords.firstOrNull() ?: error("City not found")
        fetchWeatherForLocation(location)
    }

    suspend fun searchCities(cityName: String): List<GeocodingResponse> {
        return repository.getCoordinates(cityName)
    }

    suspend fun fetchWeatherForLocation(location: GeocodingResponse): SearchWeatherResult =
        coroutineScope {
            val currentWeatherDeferred =
                async { repository.getCurrentWeather(location.lat, location.lon) }
            val forecastDeferred = async { repository.getForecast(location.lat, location.lon) }

            val fullName =
                "${location.name}${location.state?.let { ", $it" } ?: ""}, ${location.country}"
            repository.saveLastCity(fullName)

            SearchWeatherResult(
                currentWeather = currentWeatherDeferred.await(),
                forecast = forecastDeferred.await()
            )
        }
}
