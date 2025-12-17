package good.space.runnershi.model.domain

data class RunResult(
    val id: String? = null, // 서버에서 발급받을 ID
    val totalDistanceMeters: Double,
    val durationSeconds: Long, // 실제 러닝 시간 (PAUSE 시간 제외)
    val totalSeconds: Long, // 휴식시간을 포함한 총 시간 (시작부터 종료까지)
    val pathSegments: List<List<LocationModel>>, // 경로 데이터
    val calories: Int, // (거리 * 몸무게 * 계수)로 추후 계산
    val startedAt: Long = System.currentTimeMillis(), // 러닝 시작 시점
    val avgPace: String // "05'30''" 형태
)

