package com.example.myweatherapp.features.forecast.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.myweatherapp.domain.model.ForecastItem
import com.example.myweatherapp.domain.model.GeocodingResponse
import com.example.myweatherapp.domain.model.MainWeather
import com.example.myweatherapp.domain.model.WeatherDescription
import com.example.myweatherapp.features.forecast.types.ForecastIntent
import com.example.myweatherapp.features.forecast.types.ForecastState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastScreen(
    state: ForecastState,
    dispatch: (ForecastIntent) -> Unit
) {
    val focusManager = LocalFocusManager.current

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = state.cityName,
                onValueChange = { cityName: String ->
                    dispatch(
                        ForecastIntent.CityNameChanged(
                            cityName
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("forecast_search"),
                label = { Text("Search City") },
                leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    dispatch(ForecastIntent.SearchSubmitted)
                    focusManager.clearFocus()
                }),
                shape = MaterialTheme.shapes.medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    state.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    state.errorMessage != null -> {
                        Text(
                            text = state.errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    state.searchResults.isNotEmpty() -> {
                        ForecastCitySearchResults(state.searchResults) { location ->
                            dispatch(ForecastIntent.CitySelected(location))
                        }
                    }

                    state.forecast != null -> ForecastList(state.forecast.list)
                    else -> Text(
                        "No forecast data available",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun ForecastCitySearchResults(
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
                    text = item.weather.firstOrNull()?.description?.replaceFirstChar { it.uppercase() }
                        ?: "",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${item.main.temp.roundToInt()}°C",
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
