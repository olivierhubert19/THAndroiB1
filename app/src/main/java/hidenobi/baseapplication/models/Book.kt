package hidenobi.baseapplication.models

import androidx.annotation.ColorRes

data class Book(
    val id: Int,
    var name: String,
    var author: String,
    var publishedDate: String,
    var isScience: Boolean = false,
    var isNovel: Boolean = false,
    var isKid: Boolean = false,
    @ColorRes val color: Int,
)

