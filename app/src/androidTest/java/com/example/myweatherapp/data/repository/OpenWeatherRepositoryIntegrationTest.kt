package com.example.myweatherapp.data.repository

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.myweatherapp.data.local.WeatherPreferences
import com.example.myweatherapp.data.remote.WeatherApi
import com.example.myweatherapp.domain.model.CurrentWeatherResponse
import com.example.myweatherapp.domain.model.ForecastItem
import com.example.myweatherapp.domain.model.ForecastResponse
import com.example.myweatherapp.domain.model.GeocodingResponse
import com.example.myweatherapp.domain.model.MainWeather
import com.example.myweatherapp.domain.model.WeatherDescription
import com.example.myweatherapp.domain.model.Wind
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

private val Context.dataStore by preferencesDataStore(name = "test_weather_prefs")

@RunWith(AndroidJUnit4::class)
class OpenWeatherRepositoryIntegrationTest {
    @Test
    fun repositoryDelegatesWeatherCallsAndPersistsLastCity() = runBlocking {
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

        var capturedCityQuery: String? = null
        var capturedWeatherLocation: Pair<Double, Double>? = null
        var capturedForecastLocation: Pair<Double, Double>? = null

        val json = Json { ignoreUnknownKeys = true }

        val httpClient = HttpClient(
            MockEngine { request ->
                val url = request.url.toString()
                when {
                    url.contains("/geo/1.0/direct") -> {
                        capturedCityQuery = request.url.parameters["q"]
                        respond(
                            content = json.encodeToString(coordinates),
                            status = HttpStatusCode.OK,
                            headers = headersOf(HttpHeaders.ContentType to listOf("application/json"))
                        )
                    }

                    url.contains("/data/2.5/weather") -> {
                        val lat = request.url.parameters["lat"]?.toDouble() ?: 0.0
                        val lon = request.url.parameters["lon"]?.toDouble() ?: 0.0
                        capturedWeatherLocation = lat to lon
                        respond(
                            content = json.encodeToString(currentWeather),
                            status = HttpStatusCode.OK,
                            headers = headersOf(HttpHeaders.ContentType to listOf("application/json"))
                        )
                    }

                    url.contains("/data/2.5/forecast") -> {
                        val lat = request.url.parameters["lat"]?.toDouble() ?: 0.0
                        val lon = request.url.parameters["lon"]?.toDouble() ?: 0.0
                        capturedForecastLocation = lat to lon
                        respond(
                            content = json.encodeToString(forecast),
                            status = HttpStatusCode.OK,
                            headers = headersOf(HttpHeaders.ContentType to listOf("application/json"))
                        )
                    }

                    else -> respond("Not found", HttpStatusCode.NotFound)
                }
            }
        ) {
            install(ContentNegotiation) {
                json(json)
            }
        }

        val api = WeatherApi(client = httpClient, apiKey = "test-key")
        val preferences =
            WeatherPreferences(InstrumentationRegistry.getInstrumentation().targetContext.dataStore)
        val repository = OpenWeatherRepository(api, preferences)
        val city = "Repository City ${System.nanoTime()}"

        val resultCoordinates = repository.getCoordinates(city)
        val resultCurrentWeather = repository.getCurrentWeather(51.5, -0.1)
        val resultForecast = repository.getForecast(51.5, -0.1)
        repository.saveLastCity(city)

        assertEquals(coordinates, resultCoordinates)
        assertEquals(currentWeather, resultCurrentWeather)
        assertEquals(forecast, resultForecast)
        assertEquals(city, capturedCityQuery)
        assertEquals(51.5 to -0.1, capturedWeatherLocation)
        assertEquals(51.5 to -0.1, capturedForecastLocation)
        assertEquals(city, repository.getLastCity().first())
    }
}
