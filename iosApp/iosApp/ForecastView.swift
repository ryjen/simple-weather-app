import SwiftUI
import shared

struct ForecastView: View {
    @ObservedObject var observable: WeatherObservable

    var body: some View {
        List {
            if let items = observable.state.forecast?.list {
                ForEach(items, id: \.dt) { item in
                    ForecastItemView(item: item)
                }
            } else {
                Text("No forecast data available")
            }
        }
        .navigationTitle("5-Day Forecast")
    }
}

struct ForecastItemView: View {
    let item: ForecastItem

    var body: some View {
        HStack {
            VStack(alignment: .leading) {
                Text(DateTimeUtil.shared.formatTimestamp(timestamp: item.dt))
                    .font(.headline)
                Text(item.weather.first?.description_.capitalized ?? "")
                    .font(.subheadline)
                    .foregroundColor(.secondary)
            }

            Spacer()

            Text("\(Int(item.main.temp))°C")
                .font(.title2)
                .fontWeight(.bold)
                .foregroundColor(.secondaryOrange)

            AsyncImage(url: URL(string: "https://openweathermap.org/img/wn/\(item.weather.first?.icon ?? "")@2x.png")) { image in
                image.resizable()
            } placeholder: {
                ProgressView()
            }
            .frame(width: 48, height: 48)
        }
        .padding(.vertical, 4)
    }
}

extension ForecastItem: Identifiable {
    public var id: Int64 { dt }
}
