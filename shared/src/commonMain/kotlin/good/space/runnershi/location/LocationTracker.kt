package good.space.runnershi.location

import good.space.runnershi.model.domain.LocationModel
import kotlinx.coroutines.flow.Flow

// 플랫폼별 구현체가 따라야 할 인터페이스
interface LocationTracker {
    // 위치 업데이트를 Flow(스트림) 형태로 제공
    fun startTracking(): Flow<LocationModel>
    
    // 위치 추적 중단 (필요시 리소스 해제)
    fun stopTracking()
}

