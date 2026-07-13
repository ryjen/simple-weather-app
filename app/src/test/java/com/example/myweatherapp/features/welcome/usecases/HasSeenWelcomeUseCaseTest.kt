package com.example.myweatherapp.features.welcome.usecases

import com.example.myweatherapp.testing.FakeOnboardingRepository
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HasSeenWelcomeUseCaseTest {
    @Test
    fun returnsRepositoryState() {
        assertFalse(HasSeenWelcomeUseCase(FakeOnboardingRepository(hasSeenWelcome = false))())
        assertTrue(HasSeenWelcomeUseCase(FakeOnboardingRepository(hasSeenWelcome = true))())
    }
}
