import Foundation
import shared
import Combine

class WelcomeObservable: ObservableObject {
    @Published var state: WelcomeState = WelcomeState.shared
    @Published var navigationEffect: WelcomeEffect? = nil

    private let interactor = IosWelcomeInteractor()
    private var stateJob: Kotlinx_coroutines_coreJob? = nil
    private var effectJob: Kotlinx_coroutines_coreJob? = nil

    init() {
        stateJob = interactor.state.subscribe(
            onEach: { [weak self] state in
                self?.state = state
            },
            onComplete: { },
            onThrow: { error in }
        )

        effectJob = interactor.effects.subscribe(
            onEach: { [weak self] effect in
                self?.navigationEffect = effect
            },
            onComplete: { },
            onThrow: { error in }
        )
    }

    func dispatch(_ intent: WelcomeIntent) {
        interactor.dispatch(intent: intent)
    }

    deinit {
        stateJob?.cancel(cause: nil)
        effectJob?.cancel(cause: nil)
    }
}
