import SwiftUI
import shared

@main
struct iOSApp: App {
    @State private var hasOnboarded: Bool = false

    init() {
        // Initialize Koin for iOS
        // In a real environment, the API key would be fetched from a secure source
        IosKoinHelperKt.doInitKoin(apiKey: "YOUR_API_KEY_HERE") { _ in }
    }

    var body: some Scene {
        WindowGroup {
            if hasOnboarded {
                CurrentWeatherView()
            } else {
                WelcomeView(onStart: {
                    hasOnboarded = true
                })
            }
        }
    }
}
