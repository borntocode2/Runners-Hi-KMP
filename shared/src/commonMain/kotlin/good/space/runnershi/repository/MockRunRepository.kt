package good.space.runnershi.repository

import good.space.runnershi.model.domain.RunResult
import good.space.runnershi.model.dto.running.PersonalBestResponse
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlinx.datetime.Instant

// [가짜 구현체] 실제 API 연동 전까지 사용할 Stub
class MockRunRepository : RunRepository {
    override suspend fun saveRun(runResult: RunResult): Result<String> {
        // 추후 여기에 Ktor 또는 Retrofit 코드가 들어갑니다.
        println("📡 [Mock Server] Uploading Run Data...")
        println("   - Distance: ${runResult.totalDistanceMeters}m")
        println("   - Duration (실제 러닝 시간): ${runResult.duration.inWholeSeconds}s")
        println("   - Total (휴식 포함): ${runResult.totalTime.inWholeSeconds}s")
        
        // 1초 딜레이로 네트워크 통신 흉내
        kotlinx.coroutines.delay(1000) 
        
        return Result.success("SERVER_RUN_ID_12345")
    }
    
    override suspend fun getPersonalBest(): Result<PersonalBestResponse?> {
        kotlinx.coroutines.delay(500) // 로딩 흉내
        
        // 가짜 데이터 반환
        return Result.success(
            PersonalBestResponse(
                distanceMeters = 12500.0, // 12.5 km
                duration = 4500L.toDuration(DurationUnit.SECONDS),   // 1시간 15분 (4500초)
                startedAt = Instant.parse("2024-05-05T07:30:00Z")
            )
        )
    }
}

