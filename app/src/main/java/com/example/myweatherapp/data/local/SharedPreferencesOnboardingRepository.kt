package com.example.myweatherapp.data.local

import android.content.Context
import com.example.myweatherapp.domain.repository.OnboardingRepository

private const val APP_PREFS_NAME = "weather_prefs"
private const val HAS_SEEN_WELCOME_KEY = "has_seen_welcome"

class SharedPreferencesOnboardingRepository(
    context: Context
) : OnboardingRepository {
    private val preferences = context.applicationContext.getSharedPreferences(
        APP_PREFS_NAME,
        Context.MODE_PRIVATE
    )

    override fun hasSeenWelcome(): Boolean {
        return preferences.getBoolean(HAS_SEEN_WELCOME_KEY, false)
    }

    override fun markWelcomeSeen() {
        preferences.edit().putBoolean(HAS_SEEN_WELCOME_KEY, true).apply()
    }

    fun clear() {
        preferences.edit().clear().commit()
    }
}
