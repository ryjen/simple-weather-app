package com.example.myweatherapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.myweatherapp.domain.model.ForecastItem
import com.example.myweatherapp.domain.types.ForecastIntent
import com.example.myweatherapp.domain.types.ForecastState
import com.example.myweatherapp.util.DateTimeUtil

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
                        onClick = { dispatch(ForecastIntent.BackClicked) }
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
            val forecast = state.forecast
            when {
                forecast != null -> ForecastList(forecast.list)
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
                    text = DateTimeUtil.formatTimestamp(item.dt),
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
