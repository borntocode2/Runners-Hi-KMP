package good.space.runnershi.ui.running

import good.space.runnershi.model.domain.location.LocationModel
import good.space.runnershi.model.dto.running.LongestDistance
import good.space.runnershi.state.PauseType

data class RunningUiState(
    val currentLocation: LocationModel? = null,
    val pathSegments: List<List<LocationModel>> = emptyList(),
    val totalDistanceMeters: Double = 0.0,
    val durationSeconds: Long = 0L,
    val currentPace: String = "-'--''",
    val currentCalories: Int = 0,
    val isRunning: Boolean = false,
    val personalBest: LongestDistance? = null,
    val pauseType: PauseType = PauseType.NONE,
    val vehicleWarningCount: Int = 0,
    val uploadState: UploadState
)
