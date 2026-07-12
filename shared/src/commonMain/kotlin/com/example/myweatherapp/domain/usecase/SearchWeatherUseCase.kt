package com.example.myweatherapp.domain.usecase

import com.example.myweatherapp.domain.model.CurrentWeatherResponse
import com.example.myweatherapp.domain.model.ForecastResponse
import com.example.myweatherapp.domain.repository.WeatherRepository

data class SearchWeatherResult(
    val currentWeather: CurrentWeatherResponse,
    val forecast: ForecastResponse
)

class SearchWeatherUseCase(
    private val repository: WeatherRepository
) {
    suspend fun execute(cityName: String): SearchWeatherResult {
        val coords = repository.getCoordinates(cityName)
        val location = coords.firstOrNull() ?: error("City not found")
        val currentWeather = repository.getCurrentWeather(location.lat, location.lon)
        val forecast = repository.getForecast(location.lat, location.lon)

        repository.saveLastCity(cityName)

        return SearchWeatherResult(
            currentWeather = currentWeather,
            forecast = forecast
        )
    }
}
