package com.example.myweatherapp.data.repository

import com.example.myweatherapp.BuildConfig
import com.example.myweatherapp.data.local.WeatherPreferences
import com.example.myweatherapp.data.model.CurrentWeatherResponse
import com.example.myweatherapp.data.model.ForecastResponse
import com.example.myweatherapp.data.model.GeocodingResponse
import com.example.myweatherapp.data.remote.WeatherApiService
import com.example.myweatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow

class OpenWeatherRepository(
    private val apiService: WeatherApiService,
    private val preferences: WeatherPreferences
) : WeatherRepository {
    private val apiKey = BuildConfig.OPENWEATHER_API_KEY

    override suspend fun getCoordinates(cityName: String): List<GeocodingResponse> {
        return apiService.getCoordinates(cityName, apiKey = apiKey)
    }

    override suspend fun getCurrentWeather(lat: Double, lon: Double): CurrentWeatherResponse {
        return apiService.getCurrentWeather(lat, lon, apiKey = apiKey)
    }

    override suspend fun getForecast(lat: Double, lon: Double): ForecastResponse {
        return apiService.getForecast(lat, lon, apiKey = apiKey)
    }

    override fun getLastCity(): Flow<String?> = preferences.lastCityFlow

    override suspend fun saveLastCity(city: String) {
        preferences.saveLastCity(city)
    }
}
