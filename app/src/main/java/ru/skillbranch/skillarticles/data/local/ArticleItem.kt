package ru.skillbranch.skillarticles.data.local

import java.util.*

data class ArticleItem(
    val date: String, //TODO Date
    val author: String,
    val title: String,
    val description: String,
    val category: ArticleCategory,
    val posterUrl: String, //URL
    val likesCount: Int,
    val commentsCount: Int,
    val readDuration: Int,
    val isBookmark: Boolean
) {

}

enum class ArticleCategory {
    SKILL_BRANCH
}
