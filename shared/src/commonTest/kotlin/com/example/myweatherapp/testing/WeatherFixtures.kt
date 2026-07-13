package com.example.myweatherapp.testing

import com.example.myweatherapp.domain.model.CurrentWeatherResponse
import com.example.myweatherapp.domain.model.ForecastItem
import com.example.myweatherapp.domain.model.ForecastResponse
import com.example.myweatherapp.domain.model.GeocodingResponse
import com.example.myweatherapp.domain.model.MainWeather
import com.example.myweatherapp.domain.model.WeatherDescription
import com.example.myweatherapp.domain.model.Wind

fun sampleCoordinates(): GeocodingResponse {
    return GeocodingResponse(
        name = "London",
        lat = 51.5072,
        lon = -0.1276,
        country = "GB"
    )
}

fun sampleWeather(): CurrentWeatherResponse {
    return CurrentWeatherResponse(
        weather = listOf(WeatherDescription("Clear", "clear sky", "01d")),
        main = MainWeather(25.0, 27.0, 50, 1013),
        wind = Wind(5.0),
        name = "London",
        dt = 1625068800
    )
}

fun sampleForecast(): ForecastResponse {
    return ForecastResponse(
        list = listOf(
            ForecastItem(
                dt = 1625068800,
                main = MainWeather(20.0, 22.0, 60, 1012),
                weather = listOf(WeatherDescription("Clouds", "few clouds", "02d")),
                dtTxt = "2021-06-30 12:00:00"
            )
        )
    )
}
