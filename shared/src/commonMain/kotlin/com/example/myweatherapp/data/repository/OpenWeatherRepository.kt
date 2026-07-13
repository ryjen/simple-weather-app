package com.example.myweatherapp.data.repository

import com.example.myweatherapp.data.local.WeatherPreferences
import com.example.myweatherapp.data.remote.WeatherApi
import com.example.myweatherapp.domain.model.CurrentWeatherResponse
import com.example.myweatherapp.domain.model.ForecastResponse
import com.example.myweatherapp.domain.model.GeocodingResponse
import com.example.myweatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow

class OpenWeatherRepository(
    private val api: WeatherApi,
    private val preferences: WeatherPreferences
) : WeatherRepository {

    override suspend fun getCoordinates(cityName: String): List<GeocodingResponse> {
        return api.getCoordinates(cityName)
    }

    override suspend fun getCurrentWeather(lat: Double, lon: Double): CurrentWeatherResponse {
        return api.getCurrentWeather(lat, lon)
    }

    override suspend fun getForecast(lat: Double, lon: Double): ForecastResponse {
        return api.getForecast(lat, lon)
    }

    override fun getLastCity(): Flow<String?> = preferences.lastCityFlow

    override suspend fun saveLastCity(city: String) {
        preferences.saveLastCity(city)
    }
}
