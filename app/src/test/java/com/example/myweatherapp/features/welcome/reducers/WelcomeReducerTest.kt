package com.example.myweatherapp.features.welcome.reducers

import com.example.myweatherapp.features.welcome.types.WelcomeIntent
import com.example.myweatherapp.features.welcome.types.WelcomeState
import org.junit.Assert.assertSame
import org.junit.Test

class WelcomeReducerTest {
    @Test
    fun getStartedClickedDoesNotMutateState() {
        val next = welcomeReducer(WelcomeState, WelcomeIntent.GetStartedClicked)

        assertSame(WelcomeState, next)
    }
}
