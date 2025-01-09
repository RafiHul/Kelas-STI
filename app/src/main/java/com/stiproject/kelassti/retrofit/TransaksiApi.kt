package com.stiproject.kelassti.retrofit

import com.stiproject.kelassti.model.response.transaksi.TransaksiDataResponse
import retrofit2.Response
import retrofit2.http.GET

interface TransaksiApi {
    @GET("/transaksi")
    suspend fun getTransaksi(): Response<TransaksiDataResponse>
}