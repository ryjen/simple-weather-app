package com.example.myweatherapp.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeocodingResponse(
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String,
    val state: String? = null
)

@JsonClass(generateAdapter = true)
data class CurrentWeatherResponse(
    val weather: List<WeatherDescription>,
    val main: MainWeather,
    val wind: Wind,
    val name: String,
    val dt: Long
)

@JsonClass(generateAdapter = true)
data class WeatherDescription(
    val main: String,
    val description: String,
    val icon: String
)

@JsonClass(generateAdapter = true)
data class MainWeather(
    val temp: Double,
    @Json(name = "feels_like") val feelsLike: Double,
    val humidity: Int,
    val pressure: Int
)

@JsonClass(generateAdapter = true)
data class Wind(
    val speed: Double
)

@JsonClass(generateAdapter = true)
data class ForecastResponse(
    val list: List<ForecastItem>
)

@JsonClass(generateAdapter = true)
data class ForecastItem(
    val dt: Long,
    val main: MainWeather,
    val weather: List<WeatherDescription>,
    @Json(name = "dt_txt") val dtTxt: String
)
