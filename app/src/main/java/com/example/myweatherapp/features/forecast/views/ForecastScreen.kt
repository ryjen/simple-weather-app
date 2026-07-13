package com.example.myweatherapp.features.forecast.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myweatherapp.features.forecast.types.DailyForecast
import com.example.myweatherapp.features.forecast.types.ForecastIntent
import com.example.myweatherapp.features.forecast.types.ForecastState
import com.example.myweatherapp.features.forecast.types.HourlyForecast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastScreen(
    state: ForecastState,
    dispatch: (ForecastIntent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("5-Day Forecast") },
                navigationIcon = {
                    IconButton(
                        onClick = { dispatch(ForecastIntent.BackClicked) },
                        modifier = Modifier.testTag("forecast_back")
                    ) {
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
            when {
                state.days.isNotEmpty() -> ForecastList(state.days)
                state.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                else -> Text(
                    "No forecast data available",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun ForecastList(days: List<DailyForecast>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(days) { day ->
            ForecastDayView(day)
        }
    }
}

@Composable
fun ForecastDayView(day: DailyForecast) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = day.dateLabel,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(day.hours) { hour ->
                    ForecastHourView(hour)
                }
            }
        }
    }
}

@Composable
fun ForecastHourView(hour: HourlyForecast) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = hour.timeLabel,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
        AsyncImage(
            model = "https://openweathermap.org/img/wn/${hour.icon}.png",
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )
        Text(
            text = "${hour.tempCelsius}°C",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Feels like ${hour.feelsLikeCelsius}°C",
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = hour.description,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun ForecastScreenPreview() {
    MaterialTheme {
        ForecastList(
            days = listOf(
                DailyForecast(
                    dateLabel = "Wed, Jun 30",
                    hours = listOf(
                        HourlyForecast("12:00", 20, 22, "Few clouds", "02d"),
                        HourlyForecast("15:00", 18, 17, "Light rain", "10d")
                    )
                )
            )
        )
    }
}
