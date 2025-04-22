package com.stiproject.kelassti.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.stiproject.kelassti.data.model.response.kas.KasData
import com.stiproject.kelassti.data.remote.RetrofitInstance

@Suppress("SYNTHETIC_PROPERTY_WITHOUT_JAVA_ORIGIN")
// TODO: ini pelajari masih bingung dan tidak jelas
class KasPagingSource: PagingSource<Int, KasData>() {

    private val kasService = RetrofitInstance.getTransaksiService

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, KasData> {
        return try {
            val page = params.key ?: 1
            val response = kasService.getKasPage(page.toString(), params.loadSize.toString())
            val body = response.body()!!
            val data = body.data
            val nextPageUrl = body.page.Links[1].url
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

    override fun getRefreshKey(state: PagingState<Int, KasData>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}