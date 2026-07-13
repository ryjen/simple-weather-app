package com.example.myweatherapp.features.welcome.reducers

import com.example.myweatherapp.domain.reducer.welcomeReducer
import com.example.myweatherapp.domain.types.WelcomeIntent
import com.example.myweatherapp.domain.types.WelcomeState
import kotlin.test.Test
import kotlin.test.assertSame

class WelcomeReducerTest {
    @Test
    fun getStartedClickedDoesNotMutateState() {
        val next = welcomeReducer(WelcomeState, WelcomeIntent.GetStartedClicked)

        assertSame(WelcomeState, next)
    }
}
