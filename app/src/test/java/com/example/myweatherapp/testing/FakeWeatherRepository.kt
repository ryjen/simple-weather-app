package com.example.myweatherapp.testing

import com.example.myweatherapp.data.model.CurrentWeatherResponse
import com.example.myweatherapp.data.model.ForecastResponse
import com.example.myweatherapp.data.model.GeocodingResponse
import com.example.myweatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeWeatherRepository : WeatherRepository {
    var coordinates: List<GeocodingResponse> = listOf(sampleCoordinates())
    var currentWeather: CurrentWeatherResponse = sampleWeather()
    var forecast: ForecastResponse = sampleForecast()
    var lastCity: String? = null
    var savedCity: String? = null
    var coordinateQuery: String? = null
    var currentWeatherLocation: Pair<Double, Double>? = null
    var forecastLocation: Pair<Double, Double>? = null

    override suspend fun getCoordinates(cityName: String): List<GeocodingResponse> {
        coordinateQuery = cityName
        return coordinates
    }

    override suspend fun getCurrentWeather(lat: Double, lon: Double): CurrentWeatherResponse {
        currentWeatherLocation = lat to lon
        return currentWeather
    }

    override suspend fun getForecast(lat: Double, lon: Double): ForecastResponse {
        forecastLocation = lat to lon
        return forecast
    }

    override fun getLastCity(): Flow<String?> = flowOf(lastCity)

    override suspend fun saveLastCity(city: String) {
        savedCity = city
    }
}
