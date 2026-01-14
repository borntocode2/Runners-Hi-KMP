package good.space.runnershi.repository

import good.space.runnershi.model.domain.location.LocationModel

interface LocalRunningDataSource {
    suspend fun startRun()

    suspend fun saveLocation(location: LocationModel, totalDist: Double, duration: Long)

    suspend fun incrementSegmentIndex()

    suspend fun finishRun()

    suspend fun discardRun()
    
    suspend fun discardAllRuns()
}
