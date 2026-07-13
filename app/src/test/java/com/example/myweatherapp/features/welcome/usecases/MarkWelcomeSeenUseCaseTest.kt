package com.example.myweatherapp.features.welcome.usecases

import com.example.myweatherapp.testing.FakeOnboardingRepository
import org.junit.Assert.assertTrue
import org.junit.Test

class MarkWelcomeSeenUseCaseTest {
    @Test
    fun marksWelcomeSeen() {
        val repository = FakeOnboardingRepository()

        MarkWelcomeSeenUseCase(repository)()

        assertTrue(repository.hasSeenWelcome())
    }
}
