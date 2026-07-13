package com.example.myweatherapp

import android.content.Context
import com.example.myweatherapp.data.local.SharedPreferencesOnboardingRepository
import com.example.myweatherapp.data.local.WeatherPreferences
import com.example.myweatherapp.data.remote.WeatherApiService
import com.example.myweatherapp.data.repository.OpenWeatherRepository
import com.example.myweatherapp.features.welcome.usecases.HasSeenWelcomeUseCase
import com.example.myweatherapp.features.welcome.usecases.MarkWelcomeSeenUseCase
import com.example.myweatherapp.features.weather.interactors.WeatherInteractor
import com.example.myweatherapp.features.weather.usecases.LoadLastCityUseCase
import com.example.myweatherapp.features.weather.usecases.SearchWeatherUseCase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class WeatherAppGraph(context: Context) {
    private val apiService: WeatherApiService
    private val repository: OpenWeatherRepository
    private val onboardingRepository = SharedPreferencesOnboardingRepository(context.applicationContext)

    val hasSeenWelcome = HasSeenWelcomeUseCase(onboardingRepository)
    val markWelcomeSeen = MarkWelcomeSeenUseCase(onboardingRepository)

    init {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        apiService = Retrofit.Builder()
            .baseUrl(WeatherApiService.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(OkHttpClient.Builder().build())
            .build()
            .create(WeatherApiService::class.java)

        repository = OpenWeatherRepository(
            apiService = apiService,
            preferences = WeatherPreferences(context.applicationContext)
        )
    }

    fun createWeatherInteractor(): WeatherInteractor {
        return WeatherInteractor(
            searchWeatherUseCase = SearchWeatherUseCase(repository),
            loadLastCityUseCase = LoadLastCityUseCase(repository)
        )
    }
}
