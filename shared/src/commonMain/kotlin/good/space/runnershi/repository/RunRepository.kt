package good.space.runnershi.repository

import good.space.runnershi.model.domain.RunResult

interface RunRepository {
    // 러닝 기록을 서버에 전송하는 함수
    suspend fun saveRun(runResult: RunResult): Result<String>
}

// [가짜 구현체] 실제 API 연동 전까지 사용할 Stub
class MockRunRepository : RunRepository {
    override suspend fun saveRun(runResult: RunResult): Result<String> {
        // 추후 여기에 Ktor 또는 Retrofit 코드가 들어갑니다.
        println("📡 [Mock Server] Uploading Run Data...")
        println("   - Distance: ${runResult.totalDistanceMeters}m")
        println("   - Time: ${runResult.durationSeconds}s")
        
        // 1초 딜레이로 네트워크 통신 흉내
        kotlinx.coroutines.delay(1000) 
        
        return Result.success("SERVER_RUN_ID_12345")
    }
}

