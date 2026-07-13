package com.example.myweatherapp.data.remote

import com.example.myweatherapp.domain.model.CurrentWeatherResponse
import com.example.myweatherapp.domain.model.ForecastResponse
import com.example.myweatherapp.domain.model.GeocodingResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class WeatherApi(private val client: HttpClient, private val apiKey: String) {
    companion object {
        const val BASE_URL = "https://api.openweathermap.org"
    }

    suspend fun getCoordinates(cityName: String): List<GeocodingResponse> {
        return client.get("$BASE_URL/geo/1.0/direct") {
            parameter("q", cityName)
            parameter("limit", 1)
            parameter("appid", apiKey)
        }.body()
    }

    suspend fun getCurrentWeather(lat: Double, lon: Double): CurrentWeatherResponse {
        return client.get("$BASE_URL/data/2.5/weather") {
            parameter("lat", lat)
            parameter("lon", lon)
            parameter("units", "metric")
            parameter("appid", apiKey)
        }.body()
    }

    suspend fun getForecast(lat: Double, lon: Double): ForecastResponse {
        return client.get("$BASE_URL/data/2.5/forecast") {
            parameter("lat", lat)
            parameter("lon", lon)
            parameter("units", "metric")
            parameter("appid", apiKey)
        }.body()
    }
}
