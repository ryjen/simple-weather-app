package com.example.myweatherapp.data.repository

import com.example.myweatherapp.BuildConfig
import com.example.myweatherapp.data.local.WeatherPreferences
import com.example.myweatherapp.data.model.CurrentWeatherResponse
import com.example.myweatherapp.data.model.ForecastResponse
import com.example.myweatherapp.data.model.GeocodingResponse
import com.example.myweatherapp.data.remote.WeatherApiService
import com.example.myweatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow

/**
 * Concrete implementation of [WeatherRepository] that interacts with the OpenWeatherMap API
 * and local [WeatherPreferences] for persistence.
 *
 * @property apiService The Retrofit service for network requests.
 * @property preferences The local DataStore wrapper for saving user preferences.
 */
class OpenWeatherRepository(
    private val apiService: WeatherApiService,
    private val preferences: WeatherPreferences
) : WeatherRepository {
    private val apiKey = BuildConfig.OPENWEATHER_API_KEY

    /**
     * Retrieves coordinates for a given city name.
     */
    override suspend fun getCoordinates(cityName: String): List<GeocodingResponse> {
        return apiService.getCoordinates(cityName, apiKey = apiKey)
    }

    /**
     * Fetches current weather for the provided latitude and longitude.
     */
    override suspend fun getCurrentWeather(lat: Double, lon: Double): CurrentWeatherResponse {
        return apiService.getCurrentWeather(lat, lon, apiKey = apiKey)
    }

    /**
     * Fetches a multi-day forecast for the provided latitude and longitude.
     */
    override suspend fun getForecast(lat: Double, lon: Double): ForecastResponse {
        return apiService.getForecast(lat, lon, apiKey = apiKey)
    }

    /**
     * Observes the last searched city name from local storage.
     */
    override fun getLastCity(): Flow<String?> = preferences.lastCityFlow

    /**
     * Persists the last searched city name to local storage.
     */
    override suspend fun saveLastCity(city: String) {
        preferences.saveLastCity(city)
    }
}
