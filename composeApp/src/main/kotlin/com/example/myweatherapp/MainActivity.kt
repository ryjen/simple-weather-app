package com.example.myweatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.myweatherapp.BuildConfig
import com.example.myweatherapp.di.initKoin
import org.koin.android.ext.koin.androidContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        initKoin(apiKey = BuildConfig.OPENWEATHER_API_KEY) {
            androidContext(this@MainActivity)
        }

        setContent {
            App()
        }
    }
}
