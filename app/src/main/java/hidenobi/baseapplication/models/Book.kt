package hidenobi.baseapplication.models

import androidx.annotation.ColorRes
import java.time.LocalDate
import java.time.LocalDateTime


data class Book(
    var name: String,
    var author: String,
    var publishedDate: String,
    var isScience: Boolean = false,
    var isNovel: Boolean = false,
    var isKid: Boolean = false,
    @ColorRes val color: Int,
    val id: Long = System.currentTimeMillis(),
) {
    override fun equals(other: Any?): Boolean {
        return if (other is Book) {
            id == other.id
        } else {
            false
        }
    }
}

