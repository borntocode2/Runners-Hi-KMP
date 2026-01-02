package good.space.runnershi.model.dto.running

import good.space.runnershi.model.dto.user.AvatarInfo
import good.space.runnershi.model.dto.user.NewUnlockedAvatarInfo
import kotlinx.serialization.Serializable

@Serializable
data class UpdatedUserResponse(
    val userId: Long,
    val userExp: Long,
    val level: Int,
    val totalRunningDays: Long,
    val badges: List<BadgeInfo>,
    val newBadges: List<BadgeInfo>,
    val dailyQuests: List<dailyQuestInfo>,
    val avatar: AvatarInfo,
    val unlockedAvatars: List<NewUnlockedAvatarInfo>,
    val userExpProgressPercentage: Int,
    val completedQuests: List<dailyQuestInfo>,
    val runningExp: Long
)

@Serializable
data class BadgeInfo(
    val title: String ,
    val description: String,
    val exp: Long
)

@Serializable
data class dailyQuestInfo(
    val title: String,
    val exp: Long,
    val isComplete: Boolean
)
