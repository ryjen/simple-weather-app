package com.example.myweatherapp.features.welcome.usecases

import com.example.myweatherapp.domain.repository.OnboardingRepository

class HasSeenWelcomeUseCase(
    private val onboardingRepository: OnboardingRepository
) {
    operator fun invoke(): Boolean = onboardingRepository.hasSeenWelcome()
}
