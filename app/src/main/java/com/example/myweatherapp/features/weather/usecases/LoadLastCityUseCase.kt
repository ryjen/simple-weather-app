package com.example.myweatherapp.features.weather.usecases

import com.example.myweatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.firstOrNull

class LoadLastCityUseCase(
    private val repository: WeatherRepository
) {
    suspend fun execute(): String? = repository.getLastCity().firstOrNull()
}
