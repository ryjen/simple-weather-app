package com.example.myweatherapp.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeocodingResponse(
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String,
    val state: String? = null
)

@Serializable
data class CurrentWeatherResponse(
    val weather: List<WeatherDescription>,
    val main: MainWeather,
    val wind: Wind,
    val name: String,
    val dt: Long
)

@Serializable
data class WeatherDescription(
    val main: String,
    val description: String,
    val icon: String
)

@Serializable
data class MainWeather(
    val temp: Double,
    @SerialName("feels_like") val feelsLike: Double,
    val humidity: Int,
    val pressure: Int
)

@Serializable
data class Wind(
    val speed: Double
)

@Serializable
data class ForecastResponse(
    val list: List<ForecastItem>
)

@Serializable
data class ForecastItem(
    val dt: Long,
    val main: MainWeather,
    val weather: List<WeatherDescription>,
    @SerialName("dt_txt") val dtTxt: String
)
