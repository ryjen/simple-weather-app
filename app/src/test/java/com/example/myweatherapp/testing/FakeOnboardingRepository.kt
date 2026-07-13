package com.example.myweatherapp.testing

import com.example.myweatherapp.domain.repository.OnboardingRepository

class FakeOnboardingRepository(
    var hasSeenWelcome: Boolean = false
) : OnboardingRepository {
    override fun hasSeenWelcome(): Boolean = hasSeenWelcome

    override fun markWelcomeSeen() {
        hasSeenWelcome = true
    }

    fun clear() {
        hasSeenWelcome = false
    }
}
