package com.example.myweatherapp.domain.usecase

import com.example.myweatherapp.testing.FakeWeatherRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.Test

class LoadLastCityUseCaseTest {
    @Test
    fun executeReturnsSavedCity() = runTest {
        val repository = FakeWeatherRepository().apply { lastCity = "Paris" }

        assertEquals("Paris", LoadLastCityUseCase(repository).execute())
    }

    @Test
    fun executeReturnsNullWhenNoCitySaved() = runTest {
        val repository = FakeWeatherRepository().apply { lastCity = null }

        assertNull(LoadLastCityUseCase(repository).execute())
    }
}
