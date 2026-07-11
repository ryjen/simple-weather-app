package com.example.myweatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.myweatherapp.data.local.WeatherPreferences
import com.example.myweatherapp.data.remote.WeatherApiService
import com.example.myweatherapp.data.repository.WeatherRepository
import com.example.myweatherapp.ui.NavRoute
import com.example.myweatherapp.ui.screens.CurrentWeatherScreen
import com.example.myweatherapp.ui.screens.ForecastScreen
import com.example.myweatherapp.ui.screens.WelcomeScreen
import com.example.myweatherapp.ui.theme.MyWeatherAppTheme
import com.example.myweatherapp.viewmodel.WeatherViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyWeatherAppTheme {
                WeatherApp()
            }
        }
    }
}

@Composable
fun WeatherApp() {
    val context = LocalContext.current
    
    val repository = remember {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
            
        val apiService = Retrofit.Builder()
            .baseUrl(WeatherApiService.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
            .create(WeatherApiService::class.java)
            
        val preferences = WeatherPreferences(context)
        WeatherRepository(apiService, preferences)
    }

    val weatherViewModel: WeatherViewModel = viewModel {
        WeatherViewModel(repository)
    }

    val backStack = rememberNavBackStack(NavRoute.Welcome)

    NavDisplay(
        backStack = backStack,
        onBack = { if (backStack.size > 1) backStack.removeAt(backStack.size - 1) },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = { key ->
            when (key) {
                is NavRoute.Welcome -> {
                    NavEntry(key) {
                        WelcomeScreen(
                            onStartClick = { backStack.add(NavRoute.CurrentWeather) }
                        )
                    }
                }
                is NavRoute.CurrentWeather -> {
                    NavEntry(key) {
                        CurrentWeatherScreen(
                            viewModel = weatherViewModel,
                            onShowForecast = { backStack.add(NavRoute.Forecast) }
                        )
                    }
                }
                is NavRoute.Forecast -> {
                    NavEntry(key) {
                        ForecastScreen(
                            viewModel = weatherViewModel,
                            onBack = { backStack.removeAt(backStack.size - 1) }
                        )
                    }
                }
                else -> NavEntry(key) { }
            }
        }
    )
}
