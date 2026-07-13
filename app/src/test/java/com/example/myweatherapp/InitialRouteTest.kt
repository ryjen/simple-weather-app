package com.example.myweatherapp

import com.example.myweatherapp.ui.NavRoute
import org.junit.Assert.assertEquals
import org.junit.Test

class InitialRouteTest {
    @Test
    fun returnsWelcomeWhenOnboardingHasNotBeenSeen() {
        assertEquals(NavRoute.Welcome, initialRoute(hasSeenWelcome = false))
    }

    @Test
    fun returnsCurrentWeatherWhenOnboardingHasBeenSeen() {
        assertEquals(NavRoute.CurrentWeather, initialRoute(hasSeenWelcome = true))
    }
}
