package good.space.runnershi.percentile.repository

import good.space.runnershi.percentile.domain.RunResultReference
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface RunResultReferenceRepository : JpaRepository<RunResultReference, Long> {

    // 추후 보정하는 쿼리를 작성해야 할 가능성이 있으므로 nativequery = true 로 작성함
    @Query(
        value = """
               SELECT COUNT(*)
               FROM reference_runs
               WHERE is_valid = 1
                AND distance_bucket = :bucket
            """,
        nativeQuery = true
    )
    fun countTotal(
        @Param("bucket") bucket: String
    ): Long

    @Query(
        value = """
            SELECT COUNT(*)
            FROM reference_runs
            WHERE is_valid = 1
                AND distance_bucket = :bucket
                AND pace_sec_per_km < :myPace
        """,
        nativeQuery = true
    )
    fun countFasterThanMe(
        @Param("bucket") bucket: String,
        @Param("myPace") myPace: Int
    ): Long
}
