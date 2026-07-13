package com.example.myweatherapp.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.myweatherapp.data.local.WeatherPreferences
import org.koin.dsl.module

actual fun platformModule() = module {
    single {
        WeatherPreferences(createDataStore())
    }
}

fun createDataStore(): DataStore<Preferences> {
    // Implementation for iOS using actual DataStore factory
    // For now, this is a placeholder as the actual implementation involves expect/actual on the factory
    return null!! // Placeholder
}
