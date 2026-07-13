package com.example.myweatherapp

import androidx.compose.ui.window.ComposeUIViewController
import com.example.myweatherapp.di.initKoin
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController {
    // API Key should ideally be injected from a config or xcconfig on iOS
    // For this migration, we'll assume a placeholder or that it's handled via Koin init
    App()
}

fun initKoinIos(apiKey: String) {
    initKoin(apiKey = apiKey)
}
