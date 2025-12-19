package good.space.runnershi.global.running.mapper // 패키지는 적절히 설정

import good.space.runnershi.model.dto.running.LongestDistance
import good.space.runnershi.user.domain.User

// ⭐️ 확장 함수 정의: "User야, LongestDistance DTO로 변신해라"
fun User.toLongestDistanceDto(): LongestDistance {
    return LongestDistance(
        longestDistance = this.longestDistanceMeters
    )
}
