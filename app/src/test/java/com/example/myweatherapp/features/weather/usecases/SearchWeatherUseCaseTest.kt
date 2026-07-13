package com.example.myweatherapp.features.weather.usecases

import com.example.myweatherapp.testing.FakeWeatherRepository
import com.example.myweatherapp.testing.sampleCoordinates
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.fail
import org.junit.Test

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
        } catch (error: CityNotFoundException) {
            assertEquals(null, error.message)
        }
    }
}
