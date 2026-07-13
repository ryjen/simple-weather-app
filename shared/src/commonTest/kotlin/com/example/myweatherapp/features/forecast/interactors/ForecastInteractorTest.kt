package com.example.myweatherapp.domain.interactor

import com.example.myweatherapp.domain.types.ForecastEffect
import com.example.myweatherapp.domain.types.ForecastIntent
import com.example.myweatherapp.testing.MainDispatcherRule
import com.example.myweatherapp.testing.sampleForecast
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ForecastInteractorTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun forecastDataChangedUpdatesState() = runTest {
        val interactor = ForecastInteractor()
        val forecast = sampleForecast()

        interactor.dispatch(ForecastIntent.ForecastDataChanged(forecast, isLoading = true))

        assertSame(forecast, interactor.state.value.forecast)
        assertEquals(true, interactor.state.value.isLoading)
    }

    @Test
    fun backClickedEmitsNavigationEffect() = runTest {
        val interactor = ForecastInteractor()
        val effect = backgroundScope.launch(start = CoroutineStart.UNDISPATCHED) {
            assertEquals(ForecastEffect.NavigateBack, interactor.effects.first())
        }

        interactor.dispatch(ForecastIntent.BackClicked)

        effect.join()
    }
}
