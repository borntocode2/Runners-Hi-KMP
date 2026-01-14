// shared/src/androidMain/kotlin/good/space/runnershi/repository/AndroidRunningDataSource.kt
package good.space.runnershi.repository

import android.content.Context
import good.space.runnershi.database.AppDatabase
import good.space.runnershi.database.LocationEntity
import good.space.runnershi.database.RunSessionEntity
import good.space.runnershi.model.domain.location.LocationModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.UUID

class AndroidRunningDataSource(
    private val context: Context
) : LocalRunningDataSource {

    private val database: AppDatabase by lazy { AppDatabase.getDatabase(context) }
    private val dao by lazy { database.runningDao() }

    private var currentRunId: String? = null
    private var currentSegmentIndex: Int = 0

    // --- 버퍼링 최적화 변수들 ---
    private val locationBuffer = mutableListOf<LocationEntity>()
    private val bufferMutex = Mutex()
    private val BATCH_SIZE = 10

    // 통계 업데이트 최적화
    private var lastStatsUpdateTime: Long = 0

    override suspend fun startRun() = withContext(Dispatchers.IO) {
        // 기존 미완료 세션 정리
        val existingSession = dao.getUnfinishedSession()
        if (existingSession != null) {
            dao.deleteSessionById(existingSession.runId)
        }

        // 상태 초기화
        bufferMutex.withLock { locationBuffer.clear() }
        val runId = UUID.randomUUID().toString()
        currentRunId = runId
        currentSegmentIndex = 0
        lastStatsUpdateTime = 0

        // 세션 생성
        val session = RunSessionEntity(
            runId = runId,
            startTime = System.currentTimeMillis(),
            totalDistance = 0.0,
            durationSeconds = 0,
            isFinished = false
        )
        dao.insertSession(session)
    }

    override suspend fun saveLocation(location: LocationModel, totalDist: Double, duration: Long) {
        val runId = currentRunId ?: return

        // 1. 세션 통계 업데이트 (너무 자주 하지 않음)
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastStatsUpdateTime >= STATS_UPDATE_INTERVAL_MS) {
            withContext(Dispatchers.IO) {
                dao.updateSessionStats(runId, totalDist, duration)
            }
            lastStatsUpdateTime = currentTime
        }

        // 2. Entity 변환 (speed, accuracy 포함)
        val entity = LocationEntity(
            runSessionId = runId,
            latitude = location.latitude,
            longitude = location.longitude,
            timestamp = location.timestamp,
            segmentIndex = currentSegmentIndex,
            speed = location.speed,         // [New]
            accuracy = location.accuracy    // [New]
        )

        // 3. 버퍼에 추가 및 자동 플러시
        bufferMutex.withLock {
            locationBuffer.add(entity)
            if (locationBuffer.size >= BATCH_SIZE) {
                flushBufferLocked()
            }
        }
    }

    override suspend fun incrementSegmentIndex() {
        currentSegmentIndex++
    }

    override suspend fun finishRun() = withContext(Dispatchers.IO) {
        // 남은 데이터 강제 저장
        bufferMutex.withLock { flushBufferLocked() }

        val runId = currentRunId
        if (runId != null) {
            dao.finishSession(runId)
        }

        // 메모리 초기화
        currentRunId = null
        currentSegmentIndex = 0
        bufferMutex.withLock { locationBuffer.clear() }
    }

    override suspend fun discardRun() = withContext(Dispatchers.IO) {
        // 버퍼 비우기
        bufferMutex.withLock { locationBuffer.clear() }

        val runId = currentRunId ?: dao.getLatestSession()?.runId
        if (runId != null) {
            dao.deleteSessionById(runId)
        }

        currentRunId = null
        currentSegmentIndex = 0
    }

    override suspend fun discardAllRuns() = withContext(Dispatchers.IO) {
        // 버퍼 초기화
        bufferMutex.withLock {
            locationBuffer.clear()
        }
        
        // 모든 세션 삭제 (CASCADE로 좌표도 자동 삭제됨)
        dao.deleteAllSessions()
        currentRunId = null
        currentSegmentIndex = 0
    }

    private suspend fun flushBufferLocked() {
        if (locationBuffer.isEmpty()) return

        val locationsToSave = locationBuffer.toList()
        locationBuffer.clear()

        withContext(Dispatchers.IO) {
            dao.insertLocations(locationsToSave)
        }
    }

    companion object {
        private const val STATS_UPDATE_INTERVAL_MS = 5000L
    }
}
