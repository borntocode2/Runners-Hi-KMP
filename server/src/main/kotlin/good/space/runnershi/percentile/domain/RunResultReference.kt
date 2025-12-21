package good.space.runnershi.percentile.domain

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "reference_runs")
open class RunResultReference(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "run_date", nullable = false)
    val runDate: LocalDate,

    @Column(name = "athlete_id", nullable = false)
    val athleteId: Long,

    @Column(name = "distance_m", nullable = false)
    val distanceM: Int,

    @Column(name = "duration_sec", nullable = false)
    val durationSec: Int,

    @Column(name = "gender", nullable = false)
    val gender: String,  // "M" / "F"

    @Column(name = "age_group", nullable = false)
    val ageGroup: String, // "18 - 34" 같은 DB 값

    @Column(name = "country")
    val country: String? = null,

    @Column(name = "major")
    val major: String? = null,

    @Column(name = "pace_sec_per_km", nullable = false)
    val paceSecPerKm: Int,

    @Column(name = "distance_bucket", nullable = false)
    val distanceBucket: String,

    @Column(name = "is_valid", nullable = false)
    val isValid: Boolean
)
