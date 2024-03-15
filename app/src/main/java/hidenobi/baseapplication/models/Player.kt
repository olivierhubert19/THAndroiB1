package hidenobi.baseapplication.models

import androidx.annotation.ColorRes


data class Player(
    var name: String,
    var birthday: String,
    var gender: Gender,
    var isDefender: Boolean = false,
    var isMidfielder: Boolean = false,
    var isStriker: Boolean = false,
    @ColorRes val color: Int,
    val id: Long = System.currentTimeMillis(),
) {
    override fun equals(other: Any?): Boolean {
        return if (other is Player) {
            id == other.id
        } else {
            false
        }
    }
}

enum class Gender {
    MALE,
    FEMALE
}

