package com.example.myweatherapp.domain.reducer

import com.example.myweatherapp.domain.types.WelcomeIntent
import com.example.myweatherapp.domain.types.WelcomeState
import org.junit.Assert.assertSame
import org.junit.Test

class WelcomeReducerTest {
    @Test
    fun getStartedClickedDoesNotMutateState() {
        val next = welcomeReducer(WelcomeState, WelcomeIntent.GetStartedClicked)

        assertSame(WelcomeState, next)
    }
}
