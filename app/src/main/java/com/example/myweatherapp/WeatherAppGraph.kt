package com.example.myweatherapp

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.example.myweatherapp.BuildConfig
import com.example.myweatherapp.data.local.WeatherPreferences
import com.example.myweatherapp.data.remote.WeatherApi
import com.example.myweatherapp.data.repository.OpenWeatherRepository
import com.example.myweatherapp.features.forecast.interactors.ForecastInteractor
import com.example.myweatherapp.features.weather.interactors.WeatherInteractor
import com.example.myweatherapp.features.weather.usecases.LoadLastCityUseCase
import com.example.myweatherapp.features.weather.usecases.SearchWeatherUseCase
import com.example.myweatherapp.features.welcome.interactors.WelcomeInteractor
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private val Context.dataStore by preferencesDataStore(name = "weather_prefs")

class WeatherAppGraph(context: Context) {
    private val repository: OpenWeatherRepository

    init {
        val httpClient = HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }

        val api = WeatherApi(
            client = httpClient,
            apiKey = BuildConfig.OPENWEATHER_API_KEY
        )

        repository = OpenWeatherRepository(
            api = api,
            preferences = WeatherPreferences(context.applicationContext.dataStore)
        )
    }

    fun createWeatherInteractor(): WeatherInteractor {
        return WeatherInteractor(
            searchWeatherUseCase = SearchWeatherUseCase(repository),
            loadLastCityUseCase = LoadLastCityUseCase(repository)
        )
    }

    fun createWelcomeInteractor(): WelcomeInteractor {
        return WelcomeInteractor()
    }

    fun createForecastInteractor(): ForecastInteractor {
        return ForecastInteractor(
            searchWeatherUseCase = SearchWeatherUseCase(repository)
        )
    }
}
