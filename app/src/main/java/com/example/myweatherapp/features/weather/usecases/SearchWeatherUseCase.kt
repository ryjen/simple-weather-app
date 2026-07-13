package com.example.myweatherapp.features.weather.usecases

import com.example.myweatherapp.data.model.CurrentWeatherResponse
import com.example.myweatherapp.data.model.ForecastResponse
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

        val currentWeatherDeferred = async { repository.getCurrentWeather(location.lat, location.lon) }
        val forecastDeferred = async { repository.getForecast(location.lat, location.lon) }

        repository.saveLastCity(cityName)

        SearchWeatherResult(
            currentWeather = currentWeatherDeferred.await(),
            forecast = forecastDeferred.await()
        )
    }
}
