package com.example.myweatherapp.features.weather.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.myweatherapp.domain.model.CurrentWeatherResponse
import com.example.myweatherapp.domain.model.GeocodingResponse
import com.example.myweatherapp.domain.model.MainWeather
import com.example.myweatherapp.domain.model.WeatherDescription
import com.example.myweatherapp.domain.model.Wind
import com.example.myweatherapp.features.weather.types.WeatherIntent
import com.example.myweatherapp.features.weather.types.WeatherState
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentWeatherScreen(
    state: WeatherState,
    dispatch: (WeatherIntent) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Current Weather") },
                actions = {
                    TextButton(
                        onClick = { dispatch(WeatherIntent.ForecastClicked) },
                        modifier = Modifier.testTag("current_weather_forecast")
                    ) {
                        Text("Forecast")
                    }
                }
            )
        },
        contentWindowInsets = WindowInsets.systemBars
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = state.cityName,
                onValueChange = { dispatch(WeatherIntent.CityNameChanged(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("current_weather_search"),
                label = { Text("Search City") },
                leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    dispatch(WeatherIntent.SearchSubmitted)
                    focusManager.clearFocus()
                }),
                shape = MaterialTheme.shapes.medium
            )

            Spacer(modifier = Modifier.height(24.dp))

            when {
                state.isLoading -> CircularProgressIndicator(modifier = Modifier.padding(32.dp))
                state.errorMessage != null -> ErrorContent(state.errorMessage) {
                    dispatch(WeatherIntent.RetryRequested)
                }

                state.searchResults.isNotEmpty() -> CitySearchResults(state.searchResults) {
                    dispatch(WeatherIntent.CitySelected(it))
                }

                state.currentWeather != null -> WeatherContent(state.currentWeather)
                else -> Text(
                    "Search for a city to see the weather",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun CitySearchResults(
    results: List<GeocodingResponse>,
    onCitySelected: (GeocodingResponse) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = "Multiple cities found. Please select one:",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(8.dp)
            )
            LazyColumn {
                items(results) { location ->
                    ListItem(
                        headlineContent = { Text(location.name) },
                        supportingContent = {
                            Text("${location.state?.let { "$it, " } ?: ""}${location.country}")
                        },
                        leadingContent = {
                            Icon(Icons.Rounded.LocationOn, contentDescription = null)
                        },
                        modifier = Modifier.clickable { onCitySelected(location) }
                    )
                    if (location != results.last()) {
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherContent(weather: CurrentWeatherResponse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = weather.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            AsyncImage(
                model = "https://openweathermap.org/img/wn/${weather.weather.firstOrNull()?.icon}@4x.png",
                contentDescription = weather.weather.firstOrNull()?.description,
                modifier = Modifier.size(120.dp)
            )

            Text(
                text = "${weather.main.temp.roundToInt()}°C",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 72.sp
                )
            )

            Text(
                text = weather.weather.firstOrNull()?.description?.replaceFirstChar { it.uppercase() }
                    ?: "",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                WeatherDetailItem(
                    label = "Feels Like",
                    value = "${weather.main.feelsLike.roundToInt()}°C"
                )
                WeatherDetailItem(label = "Humidity", value = "${weather.main.humidity}%")
                WeatherDetailItem(label = "Wind", value = "${weather.wind.speed} m/s")
            }
        }
    }
}

@Composable
fun WeatherDetailItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ErrorContent(message: String, onRetry: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = Icons.Rounded.Warning,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(48.dp)
        )
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun CurrentWeatherScreenPreview() {
    MaterialTheme {
        WeatherContent(
            weather = CurrentWeatherResponse(
                weather = listOf(WeatherDescription("Clear", "clear sky", "01d")),
                main = MainWeather(25.0, 27.0, 50, 1013),
                wind = Wind(5.0),
                name = "London",
                dt = 1625068800
            )
        )
    }
}
