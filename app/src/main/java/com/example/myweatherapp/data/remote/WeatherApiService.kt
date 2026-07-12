package com.example.myweatherapp.data.remote

import com.example.myweatherapp.data.model.CurrentWeatherResponse
import com.example.myweatherapp.data.model.ForecastResponse
import com.example.myweatherapp.data.model.GeocodingResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit service interface for interacting with the OpenWeatherMap API.
 * Provides endpoints for geocoding, current weather conditions, and multi-day forecasts.
 */
interface WeatherApiService {
    /**
     * Retrieves geographical coordinates (latitude and longitude) for a given city name.
     *
     * @param cityName The name of the city to search for (e.g., "London").
     * @param limit Maximum number of results to return. Defaults to 1.
     * @param apiKey The OpenWeatherMap API key.
     * @return A list of [GeocodingResponse] matching the query.
     */
    @GET("geo/1.0/direct")
    suspend fun getCoordinates(
        @Query("q") cityName: String,
        @Query("limit") limit: Int = 1,
        @Query("appid") apiKey: String
    ): List<GeocodingResponse>

    /**
     * Fetches current weather data for a specific location using coordinates.
     *
     * @param lat Latitude of the location.
     * @param lon Longitude of the location.
     * @param units Unit of measurement (e.g., "metric" for Celsius). Defaults to "metric".
     * @param apiKey The OpenWeatherMap API key.
     * @return [CurrentWeatherResponse] containing real-time weather data.
     */
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String
    ): CurrentWeatherResponse

    /**
     * Fetches a 5-day / 3-hour forecast for a specific location using coordinates.
     *
     * @param lat Latitude of the location.
     * @param lon Longitude of the location.
     * @param units Unit of measurement (e.g., "metric" for Celsius). Defaults to "metric".
     * @param apiKey The OpenWeatherMap API key.
     * @return [ForecastResponse] containing forecast data points.
     */
    @GET("data/2.5/forecast")
    suspend fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String
    ): ForecastResponse

    companion object {
        /**
         * Base URL for the OpenWeatherMap API.
         */
        const val BASE_URL = "https://api.openweathermap.org/"
    }
}
