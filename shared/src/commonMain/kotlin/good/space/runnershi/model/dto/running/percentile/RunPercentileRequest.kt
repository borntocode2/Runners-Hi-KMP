package good.space.runnershi.model.dto.running.percentile

import kotlinx.serialization.Serializable

@Serializable
data class RunPercentileRequest(
    val totalDistanceMeters: Double,
    val durationSec: Int
)
