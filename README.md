# MyWeatherApp

A modern, vibrant weather application built with Kotlin, Jetpack Compose, and the OpenWeatherMap API. This app provides real-time weather updates and a multi-day forecast with a focus on clean design and resilient performance.

## 🚀 Features

- **Dynamic Onboarding:** A welcoming start to the weather journey.
- **Real-time Search:** Instantly retrieve weather data for any city globally.
- **5-Day Forecast:** Detailed weather trends with 3-hour increments.
- **Location Persistence:** Automatically remembers and loads your last searched city using Jetpack DataStore.
- **Material 3 Design:** A vibrant, energetic theme with full support for Light and Dark modes.
- **Resilient Networking:** Integrated error handling with a retry mechanism for API timeouts or connectivity issues.

## 🛠 Tech Stack

- **UI:** Jetpack Compose with Material 3.
- **Navigation:** Jetpack Navigation 3 (State-driven navigation).
- **Networking:** Retrofit & Moshi for API communication and JSON serialization.
- **Async:** Kotlin Coroutines & Flow.
- **Persistence:** Jetpack DataStore (Preferences).
- **Image Loading:** Coil for weather condition icons.
- **Architecture:** Clean Architecture principles with a robust Data Layer (Repository pattern).

## 📋 Application Flow

1.  **Welcome Screen:** Displayed upon launch, introducing the app's aesthetic. Users can proceed to the main interface with a single tap.
2.  **Current Weather:** The central hub where users can search for a city. It displays current temperature, "feels like" status, humidity, and wind speed.
3.  **Forecast View:** A dedicated screen showing the upcoming weather conditions and temperature trends for the next 5 days.
4.  **Auto-Load:** On subsequent launches, the app automatically fetches weather for the last searched city to provide immediate value.

## 🔑 Setup & Installation

To run this project, you will need an API key from [OpenWeatherMap](https://openweathermap.org/api).

1.  **Clone the repository.**
2.  **Get an API Key:** Sign up for a free account at OpenWeatherMap and generate an API key.
3.  **Configure API Key:** Add your key to the `local.properties` file in the root directory:
    ```properties
    OPENWEATHER_API_KEY=your_actual_api_key_here
    ```
4.  **Build and Run:** Open the project in Android Studio and run it on an emulator or physical device.

## 🧪 Testing

The project is backed by a comprehensive testing suite:
- **Unit Tests:** Business logic verification for repositories and ViewModels.
- **Integration Tests:** Verifying DataStore persistence and Repository-API interactions.
- **E2E Tests:** Full UI journey testing using Compose Test Rule.

Run tests via Gradle:
```bash
./gradlew test
./gradlew connectedAndroidTest
```

## 💡 Assumptions & Future Improvements

### Assumptions
- **Units:** The app defaults to Metric units (Celsius, m/s).
- **Language:** API responses and UI are currently optimized for English.
- **Geocoding:** The app uses the first result returned by the OpenWeather Geocoding API for city searches.

### Future Improvements
- **Location Services:** Integrate FusedLocationProvider for automatic "current location" weather.
- **Offline Support:** Implement Room database for caching weather data to support offline viewing.
- **Dynamic Theming:** Change the app's color palette dynamically based on the current weather condition (e.g., warm colors for sunny, cool colors for rain).
- **Detailed Forecasts:** Expand the forecast view to include precipitation probability and UV index.
