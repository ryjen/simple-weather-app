import SwiftUI
import shared

struct CurrentWeatherView: View {
    @StateObject private var observable = WeatherObservable()
    @State private var cityName: String = ""

    var body: some View {
        NavigationView {
            VStack {
                HStack {
                    TextField("Search City", text: $cityName, onCommit: {
                        observable.dispatch(WeatherIntentSearchSubmitted())
                    })
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .onChange(of: cityName) { newValue in
                        observable.dispatch(WeatherIntentCityNameChanged(value: newValue))
                    }

                    Button(action: {
                        observable.dispatch(WeatherIntentSearchSubmitted())
                    }) {
                        Image(systemName: "magnifyingglass")
                    }
                }
                .padding()

                if observable.state.isLoading {
                    ProgressView()
                        .padding()
                } else if let error = observable.state.errorMessage {
                    VStack {
                        Image(systemName: "exclamationmark.triangle.fill")
                            .foregroundColor(.red)
                            .font(.largeTitle)
                        Text(error)
                            .foregroundColor(.red)
                        Button("Retry") {
                            observable.dispatch(WeatherIntentRetryRequested())
                        }
                    }
                    .padding()
                } else if let weather = observable.state.currentWeather {
                    WeatherContentView(weather: weather)
                } else {
                    Text("Search for a city to see the weather")
                        .foregroundColor(.secondary)
                        .padding()
                }

                Spacer()
            }
            .navigationTitle("Current Weather")
            .navigationBarItems(trailing: NavigationLink(destination: ForecastView(observable: observable)) {
                Text("Forecast")
            })
        }
    }
}

struct WeatherContentView: View {
    let weather: CurrentWeatherResponse

    var body: some View {
        VStack(spacing: 24) {
            Text(weather.name)
                .font(.title)
                .fontWeight(.bold)

            AsyncImage(url: URL(string: "https://openweathermap.org/img/wn/\(weather.weather.first?.icon ?? "")@4x.png")) { image in
                image.resizable()
            } placeholder: {
                ProgressView()
            }
            .frame(width: 120, height: 120)

            Text("\(Int(weather.main.temp))°C")
                .font(.system(size: 72))
                .fontWeight(.black)

            Text(weather.weather.first?.description_.capitalized ?? "")
                .font(.title3)

            HStack(spacing: 40) {
                WeatherDetailItem(label: "Feels Like", value: "\(Int(weather.main.feelsLike))°C")
                WeatherDetailItem(label: "Humidity", value: "\(weather.main.humidity)%")
                WeatherDetailItem(label: "Wind", value: "\(weather.wind.speed) m/s")
            }
        }
        .padding()
        .background(Color(UIColor.secondarySystemBackground))
        .cornerRadius(24)
        .padding()
    }
}

struct WeatherDetailItem: View {
    let label: String
    let value: String

    var body: some View {
        VStack {
            Text(label)
                .font(.caption)
            Text(value)
                .font(.headline)
        }
    }
}
