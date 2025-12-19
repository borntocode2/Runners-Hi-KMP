package good.space.runnershi.mapper

import good.space.runnershi.model.domain.RunResult
import good.space.runnershi.model.dto.running.LocationPoint
import good.space.runnershi.model.dto.running.RunCreateRequest
import kotlinx.datetime.Instant

object RunMapper {
    fun mapToCreateRequest(domain: RunResult): RunCreateRequest {
        val flatLocations = mutableListOf<LocationPoint>()
        var globalOrder = 0

        // List<List<Location>> -> Flat List<LocationDto> 변환
        domain.pathSegments.forEachIndexed { segmentIndex, segment ->
            segment.forEach { loc ->
                flatLocations.add(
                    LocationPoint(
                        latitude = loc.latitude,
                        longitude = loc.longitude,
                        timestamp = Instant.fromEpochMilliseconds(loc.timestamp),
                        segmentIndex = segmentIndex,
                        sequenceOrder = globalOrder++
                    )
                )
            }
        }

        return RunCreateRequest(
            distanceMeters = domain.totalDistanceMeters,
            runningDuration = domain.duration, // Duration 타입 (실제 러닝 시간)
            totalDuration = domain.totalTime, // Duration 타입 (휴식시간 포함한 총 시간)
            startedAt = domain.startedAt, // Instant 타입
            locations = flatLocations
        )
    }
}

