import SwiftUI
import shared

struct WelcomeView: View {
    @StateObject private var observable = WelcomeObservable()
    var onStart: () -> Void

    var body: some View {
        VStack(spacing: 32) {
            Image(systemName: "sun.max.fill")
                .resizable()
                .frame(width: 120, height: 120)
                .foregroundColor(.yellow)

            VStack(spacing: 16) {
                Text("Welcome to MyWeatherApp")
                    .font(.largeTitle)
                    .fontWeight(.bold)
                    .multilineTextAlignment(.center)

                Text("Get real-time weather updates and stay prepared for the day ahead.")
                    .font(.body)
                    .foregroundColor(.secondary)
                    .multilineTextAlignment(.center)
            }
            .padding(.horizontal)

            Spacer()

            Button(action: {
                observable.dispatch(WelcomeIntentGetStartedClicked())
            }) {
                Text("Get Started")
                    .fontWeight(.semibold)
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(Color.primaryBlue)
                    .foregroundColor(.white)
                    .cornerRadius(16)
            }
            .padding(.horizontal)
        }
        .padding(.vertical, 48)
        .onReceive(observable.$navigationEffect) { effect in
            if effect is WelcomeEffectNavigateToWeather {
                onStart()
            }
        }
    }
}
