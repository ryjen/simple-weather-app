package com.example.myweatherapp.domain.interactor

import com.example.myweatherapp.domain.types.WelcomeEffect
import com.example.myweatherapp.domain.types.WelcomeIntent
import com.example.myweatherapp.testing.MainDispatcherRule
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WelcomeInteractorTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun getStartedClickedEmitsNavigationEffect() = runTest {
        val interactor = WelcomeInteractor()
        val effect = backgroundScope.launch(start = CoroutineStart.UNDISPATCHED) {
            assertEquals(WelcomeEffect.NavigateToWeather, interactor.effects.first())
        }

        interactor.dispatch(WelcomeIntent.GetStartedClicked)

        effect.join()
    }
}
