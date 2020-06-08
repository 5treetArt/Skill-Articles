package ru.skillbranch.skillarticles.viewmodels.bookmarks

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.skillbranch.skillarticles.data.models.ArticleItemData
import ru.skillbranch.skillarticles.data.repositories.ArticleStrategy
import ru.skillbranch.skillarticles.data.repositories.ArticlesDataFactory
import ru.skillbranch.skillarticles.data.repositories.ArticlesRepository
import ru.skillbranch.skillarticles.viewmodels.base.BaseViewModel
import ru.skillbranch.skillarticles.viewmodels.base.IViewModelState
import ru.skillbranch.skillarticles.viewmodels.base.Notify
import java.util.concurrent.Executors

class BookmarksViewModel(handle: SavedStateHandle) : BaseViewModel<BookmarksState>(handle, BookmarksState()) {
    private val repository = ArticlesRepository
    private val listConfig by lazy {
        PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(10)
            .setPrefetchDistance(30)
            .setInitialLoadSizeHint(50)
            .build()
    }
    private val listData = Transformations.switchMap(state) {
        when {
            it.isSearch && !it.searchQuery.isNullOrBlank() -> buildPagedList(
                repository.searchBookmark(it.searchQuery)
            )

            else -> buildPagedList(repository.allBookmark())
        }
    }

    fun observeList(
        owner: LifecycleOwner,
        onChanged: (list: PagedList<ArticleItemData>) -> Unit
    ) {
        listData.observe(owner, Observer { onChanged(it) })
    }

    private fun buildPagedList(
        dataFactory: ArticlesDataFactory
    ): LiveData<PagedList<ArticleItemData>> =
        LivePagedListBuilder<Int, ArticleItemData>(dataFactory, listConfig)
            .setFetchExecutor(Executors.newSingleThreadExecutor())
            .build()


    fun handleSearch(query: String?) {
        query ?: return
        updateState { it.copy(searchQuery = query) }
    }

    fun handleSearchMode(isSearch: Boolean) {
        updateState { it.copy(isSearch = isSearch) }
    }

    fun handleToggleBookmark(articleId: String, isBookmark: Boolean) {
        repository.updateBookmark(articleId, isBookmark)
        listData.value?.dataSource?.invalidate()
    }
}

data class BookmarksState(
    val isSearch: Boolean = false,
    val searchQuery: String? = null,
    val isLoading: Boolean = true
) : IViewModelState

class ArticlesBoundaryCallback(
    private val zeroLoadingHandle: () -> Unit,
    private val itemAtEndHandle: (ArticleItemData) -> Unit
) : PagedList.BoundaryCallback<ArticleItemData>() {
    override fun onZeroItemsLoaded() {
        zeroLoadingHandle()
    }

    override fun onItemAtEndLoaded(itemAtEnd: ArticleItemData) {
        itemAtEndHandle(itemAtEnd)
    }
}