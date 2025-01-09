package com.stiproject.kelassti.repository

import com.stiproject.kelassti.model.response.transaksi.TransaksiDataResponse
import com.stiproject.kelassti.retrofit.RetrofitInstance
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransaksiRepository @Inject constructor() {
    val transaksiService = RetrofitInstance.getTransaksiService

    suspend fun getTransaksi(): Response<TransaksiDataResponse> {
        return transaksiService.getTransaksi()
    }
}