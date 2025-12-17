package good.space.runnershi.global.running.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

/**
 * kotlin.time.Duration을 Long(밀리초)으로 변환하는 JPA 컨버터
 * MySQL의 BIGINT 컬럼과 매핑됩니다.
 */
@Converter(autoApply = true)
class KotlinDurationConverter : AttributeConverter<Duration, Long> {

    // DB 저장 시: Duration -> Long (밀리초)
    override fun convertToDatabaseColumn(attribute: Duration?): Long? {
        return attribute?.inWholeMilliseconds
    }

    // DB 조회 시: Long -> Duration
    override fun convertToEntityAttribute(dbData: Long?): Duration? {
        return dbData?.toDuration(DurationUnit.MILLISECONDS)
    }
}

