package com.example.myweatherapp.domain.repository

import com.example.myweatherapp.domain.model.CurrentWeatherResponse
import com.example.myweatherapp.domain.model.ForecastResponse
import com.example.myweatherapp.domain.model.GeocodingResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getCoordinates(cityName: String): List<GeocodingResponse>
    suspend fun getCurrentWeather(lat: Double, lon: Double): CurrentWeatherResponse
    suspend fun getForecast(lat: Double, lon: Double): ForecastResponse
    fun getLastCity(): Flow<String?>
    suspend fun saveLastCity(city: String)
}
