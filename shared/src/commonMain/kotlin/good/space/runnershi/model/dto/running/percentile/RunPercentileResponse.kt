package good.space.runnershi.model.dto.running.percentile

import kotlinx.serialization.Serializable

@Serializable
data class RunPercentileResponse(
    val topPercent: String?,         // 상위 몇 % (작을수록 잘함)
)
