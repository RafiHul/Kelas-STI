package com.stiproject.kelassti.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult
import androidx.paging.PagingState
import com.stiproject.kelassti.data.model.response.tasks.TasksData
import com.stiproject.kelassti.data.remote.RetrofitInstance

@Suppress("SYNTHETIC_PROPERTY_WITHOUT_JAVA_ORIGIN")
class TasksPagingSource: PagingSource<Int, TasksData>() {

    private val tasksService = RetrofitInstance.getTasksService

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TasksData> {
        return try {
            val page = params.key ?: 1
            val response = tasksService.getTasksPage(page.toString(), params.loadSize.toString())
            val body = response.body()!!
            val data = body.data.data
            val nextPageUrl = body.data.page.Links[1].url
            val nextPage = nextPageUrl?.let {
                Regex("page=(\\d+)").find(it)?.groupValues?.get(1)?.toInt()
            }

            LoadResult.Page(
                data = data,
                prevKey = null,
                nextKey = nextPage
            )

        } catch (e: Exception){
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, TasksData>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}