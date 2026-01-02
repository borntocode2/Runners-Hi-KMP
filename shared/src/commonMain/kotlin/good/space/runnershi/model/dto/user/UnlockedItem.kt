package good.space.runnershi.model.dto.user

import good.space.runnershi.model.type.BottomItem
import good.space.runnershi.model.type.HeadItem
import good.space.runnershi.model.type.ShoeItem
import good.space.runnershi.model.type.TopItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface UnlockedItem {

    @Serializable
    @SerialName("HEAD")
    data class Head(val item: HeadItem) : UnlockedItem

    @Serializable
    @SerialName("TOP")
    data class Top(val item: TopItem) : UnlockedItem

    @Serializable
    @SerialName("BOTTOM")
    data class Bottom(val item: BottomItem) : UnlockedItem

    @Serializable
    @SerialName("SHOES")
    data class Shoe(val item: ShoeItem) : UnlockedItem
}
