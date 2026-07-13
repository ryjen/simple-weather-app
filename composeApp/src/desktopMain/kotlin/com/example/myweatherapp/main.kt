package com.example.myweatherapp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.myweatherapp.di.initKoin

fun main() = application {
    // Desktop API key could come from environment variable or properties
    val apiKey = System.getenv("OPENWEATHER_API_KEY") ?: ""
    
    initKoin(apiKey = apiKey)

    Window(onCloseRequest = ::exitApplication, title = "MyWeatherApp") {
        App()
    }
}
