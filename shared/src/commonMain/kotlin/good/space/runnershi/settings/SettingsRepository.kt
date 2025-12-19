package good.space.runnershi.settings

/**
 * 앱 설정을 관리하는 인터페이스
 * 오토 퍼즈 기능의 On/Off 상태를 저장/조회합니다.
 */
interface SettingsRepository {
    suspend fun isAutoPauseEnabled(): Boolean
    suspend fun setAutoPauseEnabled(enabled: Boolean)
}

