package com.example.myweatherapp.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WeatherPreferences(private val dataStore: DataStore<Preferences>) {
    companion object {
        val LAST_CITY_KEY = stringPreferencesKey("last_city")
        const val DATASTORE_FILE_NAME = "weather_prefs.preferences_pb"
    }

    val lastCityFlow: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[LAST_CITY_KEY]
        }

    suspend fun saveLastCity(city: String) {
        dataStore.edit { preferences ->
            preferences[LAST_CITY_KEY] = city
        }
    }
}
