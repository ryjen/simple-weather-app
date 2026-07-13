package com.example.myweatherapp.domain.interactor

import com.example.myweatherapp.domain.types.WeatherEffect
import com.example.myweatherapp.domain.types.WeatherIntent
import com.example.myweatherapp.domain.usecase.LoadLastCityUseCase
import com.example.myweatherapp.domain.usecase.SearchWeatherUseCase
import com.example.myweatherapp.testing.FakeWeatherRepository
import com.example.myweatherapp.testing.MainDispatcherRule
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherInteractorTest {
    val mainDispatcherRule = MainDispatcherRule()

    @BeforeTest
    fun setup() {
        mainDispatcherRule.setup()
    }

    @kotlin.test.AfterTest
    fun tearDown() {
        mainDispatcherRule.tearDown()
    }

    @Test
    fun initLoadsLastCityAndSearchesWeather() = runTest {
        val repository = FakeWeatherRepository().apply { lastCity = " Berlin " }

        val interactor = interactor(repository)

        assertEquals("Berlin", interactor.state.value.cityName)
        assertFalse(interactor.state.value.isLoading)
        assertSame(repository.currentWeather, interactor.state.value.currentWeather)
        assertSame(repository.forecast, interactor.state.value.forecast)
        assertEquals("Berlin", repository.savedCity)
    }

    @Test
    fun searchSubmittedTrimsCityAndUpdatesState() = runTest {
        val repository = FakeWeatherRepository()
        val interactor = interactor(repository)

        interactor.dispatch(WeatherIntent.CityNameChanged(" London "))
        interactor.dispatch(WeatherIntent.SearchSubmitted)

        assertEquals("London", interactor.state.value.cityName)
        assertFalse(interactor.state.value.isLoading)
        assertSame(repository.currentWeather, interactor.state.value.currentWeather)
        assertSame(repository.forecast, interactor.state.value.forecast)
    }

    @Test
    fun searchSubmittedWithBlankCityDoesNothing() = runTest {
        val repository = FakeWeatherRepository()
        val interactor = interactor(repository)

        interactor.dispatch(WeatherIntent.CityNameChanged("   "))
        interactor.dispatch(WeatherIntent.SearchSubmitted)

        assertTrue(interactor.state.value.cityName.isBlank())
        assertEquals(null, repository.coordinateQuery)
    }

    @Test
    fun forecastClickedEmitsNavigationEffect() = runTest {
        val repository = FakeWeatherRepository()
        val interactor = interactor(repository)
        val effect = backgroundScope.launch(start = CoroutineStart.UNDISPATCHED) {
            assertEquals(WeatherEffect.NavigateToForecast, interactor.effects.first())
        }

        interactor.dispatch(WeatherIntent.ForecastClicked)

        effect.join()
    }

    private fun interactor(repository: FakeWeatherRepository): WeatherInteractor {
        return WeatherInteractor(
            searchWeatherUseCase = SearchWeatherUseCase(repository),
            loadLastCityUseCase = LoadLastCityUseCase(repository)
        )
    }
}
