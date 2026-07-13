package com.example.myweatherapp.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.myweatherapp.data.local.WeatherPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private val android.content.Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "weather_prefs"
)

actual fun platformModule() = module {
    single {
        WeatherPreferences(androidContext().dataStore)
    }
}
