package good.space.runnershi.viewmodel

import good.space.runnershi.location.LocationTracker
import good.space.runnershi.model.domain.LocationModel
import good.space.runnershi.util.DistanceCalculator
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RunningViewModel(
    private val locationTracker: LocationTracker
) {
    private val scope = CoroutineScope(Dispatchers.Main)
    private var timerJob: Job? = null // 타이머 제어용 Job

    // 1. 상태 변수들
    private val _currentLocation = MutableStateFlow<LocationModel?>(null)
    val currentLocation: StateFlow<LocationModel?> = _currentLocation.asStateFlow()

    private val _totalDistanceMeters = MutableStateFlow(0.0)
    val totalDistanceMeters: StateFlow<Double> = _totalDistanceMeters.asStateFlow()

    private val _pathPoints = MutableStateFlow<List<LocationModel>>(emptyList())
    val pathPoints: StateFlow<List<LocationModel>> = _pathPoints.asStateFlow()

    // 러닝 시간 (초)
    private val _durationSeconds = MutableStateFlow(0L)
    val durationSeconds: StateFlow<Long> = _durationSeconds.asStateFlow()

    // 러닝 상태 (UI 제어용)
    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()

    // 이전 위치를 기억하기 위한 변수
    private var lastLocation: LocationModel? = null

    // GPS 노이즈 필터링 임계값 (예: 2미터 미만 이동은 무시)
    private val MIN_DISTANCE_THRESHOLD = 2.0

    // 러닝 시작
    fun startRun() {
        if (_isRunning.value) return // 이미 실행 중이면 무시

        _isRunning.value = true
        
        // 데이터 초기화 (재시작 시) - 필요에 따라 정책 결정
        if (_durationSeconds.value == 0L) {
            _pathPoints.value = emptyList()
            _totalDistanceMeters.value = 0.0
            lastLocation = null
        }

        // 1. 위치 추적 시작
        scope.launch {
            locationTracker.startTracking().collect { newLocation ->
                updateRunData(newLocation)
            }
        }

        // 2. 타이머 시작
        startTimer()
    }

    // 타이머 로직
    private fun startTimer() {
        timerJob?.cancel() // 혹시 모를 기존 타이머 제거
        timerJob = scope.launch {
            while (isActive && _isRunning.value) {
                delay(1000L) // 1초 대기
                _durationSeconds.value += 1 // 1초 증가
            }
        }
    }

    // 러닝 일시정지 (Pause)
    fun pauseRun() {
        _isRunning.value = false
        timerJob?.cancel() // 타이머 멈춤
        locationTracker.stopTracking() // 위치 추적도 멈춤 (배터리 절약)
    }

    // 러닝 종료 (Stop & Reset)
    fun stopRun() {
        pauseRun() // 우선 멈춤
        // 여기서 서버로 데이터 전송 로직 호출 가능
        // sendDataToServer(...)
        
        // 상태 초기화는 사용자가 "저장" 버튼을 누른 후 수행하는 것이 보통
    }

    private fun updateRunData(newLocation: LocationModel) {
        val lastLoc = lastLocation

        if (lastLoc != null) {
            // 거리 계산 수행
            val distanceDelta = DistanceCalculator.calculateDistance(lastLoc, newLocation)

            // 노이즈 필터링: 의미 있는 거리만큼 이동했는지 확인
            if (distanceDelta >= MIN_DISTANCE_THRESHOLD) {
                _totalDistanceMeters.value += distanceDelta
                lastLocation = newLocation // 유효한 이동일 때만 갱신
                _currentLocation.value = newLocation
                
                // 경로 리스트에 좌표 추가
                // 주의: StateFlow는 객체 참조가 바뀌어야 방출(Emit)되므로, 새 리스트를 만들어 할당합니다.
                val currentList = _pathPoints.value
                _pathPoints.value = currentList + newLocation
            }
        } else {
            // 첫 위치 수신 시
            lastLocation = newLocation
            _currentLocation.value = newLocation
            
            // 시작점 추가
            _pathPoints.value = listOf(newLocation)
        }
    }
}

