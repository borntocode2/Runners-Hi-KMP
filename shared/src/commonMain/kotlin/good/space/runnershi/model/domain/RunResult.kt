package good.space.runnershi.model.domain

data class RunResult(
    val id: String? = null, // 서버에서 발급받을 ID
    val totalDistanceMeters: Double,
    val durationSeconds: Long,
    val pathSegments: List<List<LocationModel>>, // 경로 데이터
    val calories: Int, // (거리 * 몸무게 * 계수)로 추후 계산
    val startedAt: Long = System.currentTimeMillis(), // 종료 시점 (또는 시작 시점)
    val avgPace: String // "05'30''" 형태
)

