package com.stiproject.kelassti.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.stiproject.kelassti.data.model.response.transaksi.TransaksiData
import com.stiproject.kelassti.data.remote.api.TransaksiApi

@Suppress("SYNTHETIC_PROPERTY_WITHOUT_JAVA_ORIGIN")
// TODO: ini pelajari masih bingung dan tidak jelas

class TransaksiPagingSource(
    private val transaksiService: TransaksiApi
): PagingSource<Int, TransaksiData>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TransaksiData> {

        return try {
            val page = params.key ?: 1
            val response = transaksiService.getTransaksiPage(page.toString(), params.loadSize.toString())
            val body = response.body()!!
            val data = body.data
            val isNext = body.page.Links[1].url?.toInt()

            LoadResult.Page(
                data = data,
                prevKey = null,
                nextKey = if (isNext != null) page + 1 else null
            )

        } catch (e: Exception){
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, TransaksiData>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}