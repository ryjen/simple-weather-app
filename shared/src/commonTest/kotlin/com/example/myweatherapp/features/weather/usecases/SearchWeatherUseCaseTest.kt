package com.example.myweatherapp.domain.usecase

import com.example.myweatherapp.testing.FakeWeatherRepository
import com.example.myweatherapp.testing.sampleCoordinates
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.fail

class SearchWeatherUseCaseTest {
    @Test
    fun executeFetchesWeatherForFirstCoordinateAndSavesCity() = runTest {
        val repository = FakeWeatherRepository()
        val coordinate = sampleCoordinates()
        repository.coordinates = listOf(coordinate)
        val useCase = SearchWeatherUseCase(repository)

        val result = useCase.execute("London")

        assertEquals("London", repository.coordinateQuery)
        assertEquals(coordinate.lat to coordinate.lon, repository.currentWeatherLocation)
        assertEquals(coordinate.lat to coordinate.lon, repository.forecastLocation)
        assertEquals("London", repository.savedCity)
        assertSame(repository.currentWeather, result.currentWeather)
        assertSame(repository.forecast, result.forecast)
    }

    @Test
    fun executeFailsWhenCityHasNoCoordinates() = runTest {
        val repository = FakeWeatherRepository().apply { coordinates = emptyList() }
        val useCase = SearchWeatherUseCase(repository)

        try {
            useCase.execute("Nowhere")
            fail("Expected city lookup failure")
        } catch (error: IllegalStateException) {
            assertEquals("City not found", error.message)
        }
    }
}
