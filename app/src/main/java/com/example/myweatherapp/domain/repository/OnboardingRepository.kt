package com.example.myweatherapp.domain.repository

interface OnboardingRepository {
    fun hasSeenWelcome(): Boolean
    fun markWelcomeSeen()
}
