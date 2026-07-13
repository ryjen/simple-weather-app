package com.example.myweatherapp.features.welcome.usecases

import com.example.myweatherapp.domain.repository.OnboardingRepository

class MarkWelcomeSeenUseCase(
    private val onboardingRepository: OnboardingRepository
) {
    operator fun invoke() {
        onboardingRepository.markWelcomeSeen()
    }
}
