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

    // [핵심 변경] List<List<...>> 구조로 변경 (여러 개의 선분 관리)
    private val _pathSegments = MutableStateFlow<List<List<LocationModel>>>(emptyList())
    val pathSegments: StateFlow<List<List<LocationModel>>> = _pathSegments.asStateFlow()

    // 러닝 시간 (초)
    private val _durationSeconds = MutableStateFlow(0L)
    val durationSeconds: StateFlow<Long> = _durationSeconds.asStateFlow()

    // 러닝 상태 (UI 제어용)
    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()

    // 이전 위치 (거리 계산용)
    private var lastLocation: LocationModel? = null
    private val MIN_DISTANCE_THRESHOLD = 2.0

    // --- 동작 로직 ---

    fun startRun() {
        if (_isRunning.value) return

        // 초기화 로직 (처음 시작할 때만)
        if (_durationSeconds.value == 0L) {
            _totalDistanceMeters.value = 0.0
            _pathSegments.value = listOf(emptyList()) // 첫 번째 세그먼트 준비
            lastLocation = null
        } else {
            // [재개 Resume 상황]
            // 이전에 일시정지 했으므로, 새로운 세그먼트를 추가해줍니다.
            // 예: [[A,B,C]] -> [[A,B,C], []]
            _pathSegments.value = _pathSegments.value + listOf(emptyList())
            
            // [중요] 재개 시에는 lastLocation을 초기화해야 '순간이동' 거리가 더해지지 않습니다.
            lastLocation = null 
        }

        _isRunning.value = true
        startTimer()
        startLocationTracking()
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

    fun pauseRun() {
        _isRunning.value = false
        timerJob?.cancel()
        locationTracker.stopTracking()
        
        // 일시정지 시점에는 특별히 데이터를 변형하지 않아도 됩니다.
        // 재개(startRun)할 때 새 리스트를 만드는 것이 더 안전합니다.
    }

    fun stopRun() {
        pauseRun()
        // TODO: 저장 로직 (List<List<LocationModel>> 전체를 저장해야 함)
    }

    private fun startLocationTracking() {
        scope.launch {
            locationTracker.startTracking().collect { newLocation ->
                // 일시정지 상태가 아닐 때만 데이터 처리
                if (_isRunning.value) {
                    updateRunData(newLocation)
                }
            }
        }
    }

    private fun updateRunData(newLocation: LocationModel) {
        val lastLoc = lastLocation

        // 1. 거리 계산 (이전 위치가 있고, 현재 세그먼트 내에서 이어지는 경우에만)
        if (lastLoc != null) {
            val distanceDelta = DistanceCalculator.calculateDistance(lastLoc, newLocation)

            if (distanceDelta >= MIN_DISTANCE_THRESHOLD) {
                _totalDistanceMeters.value += distanceDelta
                lastLocation = newLocation
                _currentLocation.value = newLocation
                
                // 2. 경로 추가
                addPointToCurrentSegment(newLocation)
            }
        } else {
            // [세그먼트의 시작점]
            // Resume 직후거나, 맨 처음 시작일 때
            // 거리는 더하지 않고 위치만 기록함
            lastLocation = newLocation
            _currentLocation.value = newLocation
            
            // 경로 추가
            addPointToCurrentSegment(newLocation)
        }
    }

    // 현재 활성화된 마지막 세그먼트에 점을 추가하는 헬퍼 함수
    private fun addPointToCurrentSegment(point: LocationModel) {
        val currentSegments = _pathSegments.value
        if (currentSegments.isNotEmpty()) {
            val lastSegmentIndex = currentSegments.lastIndex
            val lastSegment = currentSegments[lastSegmentIndex]
            
            // 불변성 유지를 위해 새로운 리스트 생성
            val newSegment = lastSegment + point
            
            // 전체 리스트 교체
            val newSegments = currentSegments.toMutableList()
            newSegments[lastSegmentIndex] = newSegment
            
            _pathSegments.value = newSegments
        } else {
            // 방어 코드: 혹시 세그먼트가 하나도 없다면 새로 생성
            _pathSegments.value = listOf(listOf(point))
        }
    }
}

