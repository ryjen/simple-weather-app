package com.example.myweatherapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myweatherapp.data.model.*
import com.example.myweatherapp.viewmodel.WeatherUiState
import com.example.myweatherapp.viewmodel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastScreen(
    viewModel: WeatherViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("5-Day Forecast") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        contentWindowInsets = WindowInsets.systemBars
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = uiState) {
                is WeatherUiState.Success -> {
                    ForecastList(state.forecast.list)
                }
                is WeatherUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                else -> {
                    Text(
                        "No forecast data available",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun ForecastList(items: List<ForecastItem>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) { item ->
            ForecastItemView(item)
        }
    }
}

@Composable
fun ForecastItemView(item: ForecastItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = formatTimestamp(item.dt),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = item.weather.firstOrNull()?.description?.replaceFirstChar { it.uppercase() } ?: "",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${item.main.temp.toInt()}°C",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                AsyncImage(
                    model = "https://openweathermap.org/img/wn/${item.weather.firstOrNull()?.icon}.png",
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val date = Date(timestamp * 1000L)
    val sdf = SimpleDateFormat("EEE, MMM d, HH:mm", Locale.getDefault())
    return sdf.format(date)
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun ForecastScreenPreview() {
    MaterialTheme {
        ForecastList(
            items = listOf(
                ForecastItem(
                    dt = 1625068800,
                    main = MainWeather(20.0, 22.0, 60, 1012),
                    weather = listOf(WeatherDescription("Clouds", "few clouds", "02d")),
                    dtTxt = "2021-06-30 12:00:00"
                ),
                ForecastItem(
                    dt = 1625079600,
                    main = MainWeather(18.0, 20.0, 65, 1011),
                    weather = listOf(WeatherDescription("Rain", "light rain", "10d")),
                    dtTxt = "2021-06-30 15:00:00"
                )
            )
        )
    }
}
