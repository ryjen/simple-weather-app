package com.example.myweatherapp.features.weather.interactors

import com.example.myweatherapp.features.weather.types.WeatherEffect
import com.example.myweatherapp.features.weather.types.WeatherIntent
import com.example.myweatherapp.features.weather.usecases.LoadLastCityUseCase
import com.example.myweatherapp.features.weather.usecases.SearchWeatherUseCase
import com.example.myweatherapp.testing.FakeWeatherRepository
import com.example.myweatherapp.testing.MainDispatcherRule
import com.squareup.moshi.JsonDataException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherInteractorTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

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
        interactor.dispatch(WeatherIntent.CityNameChanged("London"))
        interactor.dispatch(WeatherIntent.SearchSubmitted)
        val effect = backgroundScope.launch(start = CoroutineStart.UNDISPATCHED) {
            assertEquals(WeatherEffect.NavigateToForecast, interactor.effects.first())
        }

        interactor.dispatch(WeatherIntent.ForecastClicked)

        effect.join()
    }

    @Test
    fun forecastClickedWithoutForecastDoesNotNavigate() = runTest {
        val interactor = interactor(FakeWeatherRepository())

        interactor.dispatch(WeatherIntent.ForecastClicked)

        assertEquals(null, interactor.effects.replayCache.firstOrNull())
    }

    @Test
    fun searchFailureUsesStableInvalidCityMessage() = runTest {
        val repository = FakeWeatherRepository().apply { coordinates = emptyList() }
        val interactor = interactor(repository)

        interactor.dispatch(WeatherIntent.CityNameChanged("Nowhere"))
        interactor.dispatch(WeatherIntent.SearchSubmitted)

        assertEquals("We couldn't find that city. Check the spelling and try again.", interactor.state.value.errorMessage)
    }

    @Test
    fun searchFailureUsesStableTimeoutMessage() = runTest {
        val repository = FakeWeatherRepository().apply { coordinatesError = SocketTimeoutException("timeout stack detail") }
        val interactor = interactor(repository)

        interactor.dispatch(WeatherIntent.CityNameChanged("London"))
        interactor.dispatch(WeatherIntent.SearchSubmitted)

        assertEquals("The weather service took too long to respond. Try again.", interactor.state.value.errorMessage)
    }

    @Test
    fun searchFailureUsesStableConnectivityMessage() = runTest {
        val repository = FakeWeatherRepository().apply { coordinatesError = UnknownHostException("api.openweathermap.org") }
        val interactor = interactor(repository)

        interactor.dispatch(WeatherIntent.CityNameChanged("London"))
        interactor.dispatch(WeatherIntent.SearchSubmitted)

        assertEquals(
            "We couldn't connect to the weather service. Check your connection and try again.",
            interactor.state.value.errorMessage
        )
    }

    @Test
    fun searchFailureUsesStableHttpMessage() = runTest {
        val httpError = HttpException(Response.error<Unit>(500, "server exploded".toResponseBody()))
        val repository = FakeWeatherRepository().apply { currentWeatherError = httpError }
        val interactor = interactor(repository)

        interactor.dispatch(WeatherIntent.CityNameChanged("London"))
        interactor.dispatch(WeatherIntent.SearchSubmitted)

        assertEquals("The weather service returned an error. Try again later.", interactor.state.value.errorMessage)
    }

    @Test
    fun searchFailureUsesStableMalformedResponseMessage() = runTest {
        val repository = FakeWeatherRepository().apply { currentWeatherError = JsonDataException("missing temp") }
        val interactor = interactor(repository)

        interactor.dispatch(WeatherIntent.CityNameChanged("London"))
        interactor.dispatch(WeatherIntent.SearchSubmitted)

        assertEquals("We couldn't read the weather response. Try again later.", interactor.state.value.errorMessage)
    }

    private fun interactor(repository: FakeWeatherRepository): WeatherInteractor {
        return WeatherInteractor(
            searchWeatherUseCase = SearchWeatherUseCase(repository),
            loadLastCityUseCase = LoadLastCityUseCase(repository)
        )
    }
}
