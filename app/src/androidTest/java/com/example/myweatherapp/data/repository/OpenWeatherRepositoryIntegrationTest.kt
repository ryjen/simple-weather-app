package com.example.myweatherapp.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.myweatherapp.data.local.WeatherPreferences
import com.example.myweatherapp.data.model.CurrentWeatherResponse
import com.example.myweatherapp.data.model.ForecastItem
import com.example.myweatherapp.data.model.ForecastResponse
import com.example.myweatherapp.data.model.GeocodingResponse
import com.example.myweatherapp.data.model.MainWeather
import com.example.myweatherapp.data.model.WeatherDescription
import com.example.myweatherapp.data.model.Wind
import com.example.myweatherapp.data.remote.WeatherApiService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OpenWeatherRepositoryIntegrationTest {
    @Test
    fun repositoryDelegatesWeatherCallsAndPersistsLastCity() = runBlocking {
        val apiService = FakeWeatherApiService()
        val preferences = WeatherPreferences(InstrumentationRegistry.getInstrumentation().targetContext)
        val repository = OpenWeatherRepository(apiService, preferences)
        val city = "Repository City ${System.nanoTime()}"

        val coordinates = repository.getCoordinates(city)
        val currentWeather = repository.getCurrentWeather(51.5, -0.1)
        val forecast = repository.getForecast(51.5, -0.1)
        repository.saveLastCity(city)

        assertSame(apiService.coordinates, coordinates)
        assertSame(apiService.currentWeather, currentWeather)
        assertSame(apiService.forecast, forecast)
        assertEquals(city, apiService.coordinateQuery)
        assertEquals(51.5 to -0.1, apiService.currentWeatherLocation)
        assertEquals(51.5 to -0.1, apiService.forecastLocation)
        assertEquals(city, repository.getLastCity().first())
    }
}

private class FakeWeatherApiService : WeatherApiService {
    val coordinates = listOf(
        GeocodingResponse(
            name = "London",
            lat = 51.5072,
            lon = -0.1276,
            country = "GB"
        )
    )
    val currentWeather = CurrentWeatherResponse(
        weather = listOf(WeatherDescription("Clear", "clear sky", "01d")),
        main = MainWeather(25.0, 27.0, 50, 1013),
        wind = Wind(5.0),
        name = "London",
        dt = 1625068800
    )
    val forecast = ForecastResponse(
        list = listOf(
            ForecastItem(
                dt = 1625068800,
                main = MainWeather(20.0, 22.0, 60, 1012),
                weather = listOf(WeatherDescription("Clouds", "few clouds", "02d")),
                dtTxt = "2021-06-30 12:00:00"
            )
        )
    )
    var coordinateQuery: String? = null
    var currentWeatherLocation: Pair<Double, Double>? = null
    var forecastLocation: Pair<Double, Double>? = null

    override suspend fun getCoordinates(
        cityName: String,
        limit: Int,
        apiKey: String
    ): List<GeocodingResponse> {
        coordinateQuery = cityName
        return coordinates
    }

    override suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        units: String,
        apiKey: String
    ): CurrentWeatherResponse {
        currentWeatherLocation = lat to lon
        return currentWeather
    }

    override suspend fun getForecast(
        lat: Double,
        lon: Double,
        units: String,
        apiKey: String
    ): ForecastResponse {
        forecastLocation = lat to lon
        return forecast
    }
}
