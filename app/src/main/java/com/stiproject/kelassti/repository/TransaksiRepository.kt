package com.stiproject.kelassti.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.stiproject.kelassti.model.request.KasRequest
import com.stiproject.kelassti.model.response.transaksi.TransaksiData
import com.stiproject.kelassti.model.response.transaksi.TransaksiDataByIdResponse
import com.stiproject.kelassti.model.response.transaksi.TransaksiDataResponse
import com.stiproject.kelassti.retrofit.RetrofitInstance
import com.stiproject.kelassti.retrofit.TransaksiPagingSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransaksiRepository @Inject constructor() {
    val transaksiService = RetrofitInstance.getTransaksiService

    suspend fun getAllTransaksi(): Response<TransaksiDataResponse> {
        return transaksiService.getTransaksi()
    }

    fun getTransaksiPageFlow(): Flow<PagingData<TransaksiData>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { TransaksiPagingSource(transaksiService) }
        ).flow
    }

    suspend fun postTransaksi(jwtToken: String, kasRequest: KasRequest): Response<TransaksiDataByIdResponse> {
        return transaksiService.postTransaksi(jwtToken,kasRequest)
    }

    suspend fun getTransaksiById(id: String): Response<TransaksiDataByIdResponse> {
        return transaksiService.getTransaksiById(id)
    }

    suspend fun updateTransaksiById(id: String, jwtToken: String, kasRequest: KasRequest): Response<TransaksiDataByIdResponse> {
        return transaksiService.updateTransaksiById(id,jwtToken,kasRequest)
    }
}