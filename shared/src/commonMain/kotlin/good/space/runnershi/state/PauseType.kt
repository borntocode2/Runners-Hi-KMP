package good.space.runnershi.state

/**
 * 일시정지의 원인을 구분하는 열거형
 * 사용자가 멈춘 이유에 따라 다른 UX 피드백을 제공하기 위함
 */
enum class PauseType {
    NONE,                    // 달리는 중 (정지 아님)
    USER_PAUSE,              // 사용자가 버튼을 눌러서 수동 정지
    AUTO_PAUSE_REST,         // 정지 감지 (휴식 - 정상적인 상황, 조용히 처리)
    AUTO_PAUSE_VEHICLE       // 과속 감지 (차량 이동 의심 - 경고 필요)
}

