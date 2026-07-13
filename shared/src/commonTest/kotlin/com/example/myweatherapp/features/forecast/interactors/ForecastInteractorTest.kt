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
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

@OptIn(ExperimentalCoroutinesApi::class)
class ForecastInteractorTest {
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
