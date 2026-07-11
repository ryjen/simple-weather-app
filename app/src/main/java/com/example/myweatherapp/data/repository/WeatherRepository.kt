package com.example.myweatherapp.data.repository

import com.example.myweatherapp.BuildConfig
import com.example.myweatherapp.data.local.WeatherPreferences
import com.example.myweatherapp.data.model.CurrentWeatherResponse
import com.example.myweatherapp.data.model.ForecastResponse
import com.example.myweatherapp.data.model.GeocodingResponse
import com.example.myweatherapp.data.remote.WeatherApiService
import kotlinx.coroutines.flow.Flow

class WeatherRepository(
    private val apiService: WeatherApiService,
    private val preferences: WeatherPreferences
) {
    private val apiKey = BuildConfig.OPENWEATHER_API_KEY

    suspend fun getCoordinates(cityName: String): List<GeocodingResponse> {
        return apiService.getCoordinates(cityName, apiKey = apiKey)
    }

    suspend fun getCurrentWeather(lat: Double, lon: Double): CurrentWeatherResponse {
        return apiService.getCurrentWeather(lat, lon, apiKey = apiKey)
    }

    suspend fun getForecast(lat: Double, lon: Double): ForecastResponse {
        return apiService.getForecast(lat, lon, apiKey = apiKey)
    }

    fun getLastCity(): Flow<String?> = preferences.lastCityFlow

    suspend fun saveLastCity(city: String) {
        preferences.saveLastCity(city)
    }
}
