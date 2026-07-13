import Foundation
import shared
import Combine

/**
 * A Swift ObservableObject that wraps the Kotlin Interactor.
 */
class WeatherObservable: ObservableObject {
    @Published var state: WeatherState = WeatherState(cityName: "", currentWeather: nil, forecast: nil, isLoading: false, errorMessage: nil)

    private let interactor = IosWeatherInteractor()
    private var job: Kotlinx_coroutines_coreJob? = nil

    init() {
        job = interactor.state.subscribe(
            onEach: { [weak self] state in
                self?.state = state
            },
            onComplete: { },
            onThrow: { error in }
        )
    }

    func dispatch(_ intent: WeatherIntent) {
        interactor.dispatch(intent: intent)
    }

    deinit {
        job?.cancel(cause: nil)
    }
}
