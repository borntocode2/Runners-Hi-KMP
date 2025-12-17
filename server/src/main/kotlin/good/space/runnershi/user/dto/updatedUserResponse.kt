package good.space.runnershi.user.dto

import good.space.runnershi.user.domain.User


data class UpdatedUserResponse(
    val userId: Long,
    val exp: Long
) {
    companion object {
        fun from(user: User): UpdatedUserResponse {
            return UpdatedUserResponse(
                userId = user.id ?: throw IllegalStateException("저장되지 않은 유저(ID 없음)입니다."),
                exp = user.exp
            )
        }
    }
}
