package com.example.myweatherapp.di

import com.example.myweatherapp.data.remote.WeatherApi
import com.example.myweatherapp.data.repository.OpenWeatherRepository
import com.example.myweatherapp.domain.interactor.WeatherInteractor
import com.example.myweatherapp.domain.interactor.WelcomeInteractor
import com.example.myweatherapp.domain.interactor.ForecastInteractor
import com.example.myweatherapp.domain.repository.WeatherRepository
import com.example.myweatherapp.domain.usecase.LoadLastCityUseCase
import com.example.myweatherapp.domain.usecase.SearchWeatherUseCase
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.factoryOf

fun commonModule(apiKey: String) = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                })
            }
            install(Logging) {
                level = LogLevel.BODY
            }
        }
    }

    single { WeatherApi(get(), apiKey) }
    single<WeatherRepository> { OpenWeatherRepository(get(), get()) }

    factory { SearchWeatherUseCase(get()) }
    factory { LoadLastCityUseCase(get()) }

    factory { WeatherInteractor(get(), get()) }
    factory { WelcomeInteractor() }
    factory { ForecastInteractor() }
}
