package com.example.myweatherapp.features.weather.usecases

import com.example.myweatherapp.testing.FakeWeatherRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

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
