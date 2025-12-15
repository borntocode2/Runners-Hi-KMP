package good.space.runnershi.viewmodel

import good.space.runnershi.service.ServiceController
import good.space.runnershi.state.RunningStateManager
import kotlinx.coroutines.flow.StateFlow

class RunningViewModel(
    private val serviceController: ServiceController
) {
    // 데이터는 StateManager에서 직접 구독
    val currentLocation: StateFlow<good.space.runnershi.model.domain.LocationModel?> = RunningStateManager.currentLocation
    val totalDistanceMeters: StateFlow<Double> = RunningStateManager.totalDistanceMeters
    val pathSegments: StateFlow<List<List<good.space.runnershi.model.domain.LocationModel>>> = RunningStateManager.pathSegments
    val durationSeconds: StateFlow<Long> = RunningStateManager.durationSeconds
    val isRunning: StateFlow<Boolean> = RunningStateManager.isRunning

    fun startRun() {
        if (durationSeconds.value > 0) {
            serviceController.resumeService()
        } else {
            serviceController.startService()
        }
    }

    fun pauseRun() {
        serviceController.pauseService()
    }

    fun stopRun() {
        serviceController.stopService()
    }
}
