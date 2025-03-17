package com.stiproject.kelassti.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.stiproject.kelassti.data.remote.RetrofitInstance
import com.stiproject.kelassti.data.model.request.KasRequest
import com.stiproject.kelassti.data.model.response.transaksi.TransaksiData
import com.stiproject.kelassti.data.remote.paging.TransaksiPagingSource
import com.stiproject.kelassti.util.ApiResult
import com.stiproject.kelassti.util.parseErrorMessageJsonToString
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransaksiRepository @Inject constructor() {
    val transaksiService = RetrofitInstance.getTransaksiService

    suspend fun getAllTransaksi(): ApiResult {
        val response = transaksiService.getAllTransaksi()
        val body = response.body()
        val errorBody = response.errorBody()

        if(!response.isSuccessful){
            return ApiResult.Failed(errorBody.parseErrorMessageJsonToString())
        }

        if (body == null){
            return ApiResult.Failed("Gagal Mendapatkan data kas")
        }

        return ApiResult.Success(body.message, body.data)
    }

    fun getTransaksiPage(): Flow<PagingData<TransaksiData>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { TransaksiPagingSource(transaksiService) }
        ).flow
    }

    suspend fun postTransaksi(jwtToken: String, kasRequest: KasRequest): ApiResult {

        val response = transaksiService.postTransaksi(jwtToken,kasRequest)
        val body = response.body()
        val errorBody = response.errorBody()

        if(!response.isSuccessful){
            return ApiResult.Failed(errorBody.parseErrorMessageJsonToString())
        }

        if (body == null){
            return ApiResult.Failed("Gagal Menambahkan Data")
        }

        return ApiResult.Success(body.message, null)
    }

    suspend fun getTransaksiById(id: String): ApiResult {
        val response = transaksiService.getTransaksiById(id)
        val body = response.body()

        if (!response.isSuccessful || body == null) {
            return ApiResult.Failed("Gagal Mendapatkan Data") // TODO: ini bisa pake body.message (string)
        }

        return ApiResult.Success(body.message, body.data)
    }

    suspend fun updateTransaksiById(id: String, jwtToken: String, kasRequest: KasRequest): ApiResult {
        val response = transaksiService.updateTransaksiById(id,jwtToken,kasRequest)
        val body = response.body()
        val errorBody = response.errorBody()

        if(!response.isSuccessful || body == null){
            return ApiResult.Failed(errorBody.parseErrorMessageJsonToString())
        }

        return ApiResult.Success(body.message,body.data)
    }
}

