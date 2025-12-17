package good.space.runnershi.global.running.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinInstant

/**
 * kotlinx.datetime.Instant를 java.time.Instant로 변환하는 JPA 컨버터
 * MySQL의 DATETIME/TIMESTAMP 컬럼과 매핑됩니다.
 */
@Converter(autoApply = true)
class KotlinInstantConverter : AttributeConverter<Instant, java.time.Instant> {
    
    // DB 저장 시: kotlinx -> java.time 변환
    override fun convertToDatabaseColumn(attribute: Instant?): java.time.Instant? {
        return attribute?.toJavaInstant()
    }

    // DB 조회 시: java.time -> kotlinx 변환
    override fun convertToEntityAttribute(dbData: java.time.Instant?): Instant? {
        return dbData?.toKotlinInstant()
    }
}

