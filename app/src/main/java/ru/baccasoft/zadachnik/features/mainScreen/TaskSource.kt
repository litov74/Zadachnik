package ru.baccasoft.zadachnik.features.mainScreen

import android.app.Application
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.HttpException
import ru.baccasoft.zadachnik.data.network.RetrofitApi
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class TaskSource (
    apiService: RetrofitApi,
    val navigationCurrentScreen: StateFlow<String>,
    val activeFilter: StateFlow<Int>,
    val activeArchiveFilter: StateFlow<Int>,
    val descending: StateFlow<Boolean>,
    val descendingArchive: StateFlow<Boolean>,
    private val mineFilter: StateFlow<Boolean>,
    private val fromMeFilter: StateFlow<Boolean>,
    private val mineArchiveFilter: StateFlow<Boolean>,
    private val fromMeArchiveFilter: StateFlow<Boolean>,
    private val  mainScreenSearchTextFieldState: StateFlow<String>,
    private val  archiveScreenSearchTextFieldState: StateFlow<String>,
) : PagingSource<Int, TaskModel>() {

    private val repo = apiService

    init {
        Timber.i("initTaskSource")
    }

    override fun getRefreshKey(state: PagingState<Int, TaskModel>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        val res = anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
        return res
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TaskModel> {
        val isForArchived = navigationCurrentScreen.value == "archive_screen"
        val onlyActive = navigationCurrentScreen.value == "main_screen"
        val filter = if(isForArchived) activeArchiveFilter else activeFilter
        val active = if(isForArchived) descendingArchive else descending
        val screenSearchTextFieldState = if (navigationCurrentScreen.value == "main_screen") mainScreenSearchTextFieldState else archiveScreenSearchTextFieldState
        val is_user_creator: Boolean
        val is_user_responsible: Boolean
        val mineFilterCurrent = if (navigationCurrentScreen.value == "main_screen") mineFilter else mineArchiveFilter
        val fromMeFilterCurrent = if (navigationCurrentScreen.value == "main_screen") fromMeFilter else fromMeArchiveFilter
        if (mineFilterCurrent.value) {
            is_user_creator = false
            is_user_responsible = true
        } else if (fromMeFilterCurrent.value) {
            is_user_creator = true
            is_user_responsible = false
        } else {
            is_user_creator = false
            is_user_responsible = false
        }

        val pageSize = params.loadSize
        val nextPage = params.key ?: 1

        repo.getFilteredDocuments(
            search = screenSearchTextFieldState.value,
            sort =
            if (filter.value == 0) {
                if (!active.value) {
                    "-title"
                } else {
                    "title"
                }
            } else if (filter.value == 1) {
                if (!active.value) {
                    "-responsible"
                } else {
                    "responsible"
                }
            } else if (filter.value == 2) {
                if (!active.value) {
                    "-deadline"
                } else {
                    "deadline"
                }
            } else {
                ""
            },
            page = nextPage,
            size = pageSize,
            archived = if (isForArchived) 1 else 0,
            active = if (onlyActive) 1 else 0,
            is_user_creator = if (is_user_creator) 1 else 0,
            is_user_responsible = if (is_user_responsible) 1 else 0,
        ).onSuccess {
            return LoadResult.Page(
                data = it.taskList.distinct(),
                prevKey = if (nextPage <= 1) null else nextPage - 1,
                nextKey = if (it.taskList.size < pageSize) null else nextPage + 1
            )
        }.onFailure {
            Timber.d("TaskPagingError  %s", "${it.localizedMessage}")
            return LoadResult.Error(it)
        }
        return LoadResult.Error(Throwable())
    }

}