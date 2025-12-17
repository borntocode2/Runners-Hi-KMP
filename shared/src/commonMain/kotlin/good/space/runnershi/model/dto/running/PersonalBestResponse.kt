package good.space.runnershi.model.dto.running

import kotlin.time.Duration
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class PersonalBestResponse(
    val distanceMeters: Double,   // 예: 10500.0 (10.5km)
    @Serializable(with = DurationSerializer::class)
    val duration: Duration,    // 예: 1시간
    @Serializable(with = InstantSerializer::class)
    val startedAt: Instant         // 러닝 시작 시점
)

