package ru.skillbranch.skillarticles.ui.article

import ru.skillbranch.skillarticles.data.remote.res.CommentRes

interface IArticleView {

    /**
     * показать search bar
     */
    fun showSearchBar()
    /**
     * скрыть search bar
     */
    fun hideSearchBar()

    fun clickOnComment(comment: CommentRes)
}