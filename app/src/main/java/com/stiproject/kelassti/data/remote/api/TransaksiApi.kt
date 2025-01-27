package com.stiproject.kelassti.data.remote.api

import com.stiproject.kelassti.data.model.request.KasRequest
import com.stiproject.kelassti.data.model.response.transaksi.TransaksiDataByIdResponse
import com.stiproject.kelassti.data.model.response.transaksi.TransaksiDataResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TransaksiApi {
    @GET("/transaksi")
    suspend fun getTransaksi(): Response<TransaksiDataResponse>


    @GET("/transaksi")
    suspend fun getTransaksiPage(
        @Query("page") page: String,
        @Query("size") size: String
    ): Response<TransaksiDataResponse>

    @POST("/transaksi")
    suspend fun postTransaksi(
        @Header("Authorization") jwt: String,
        @Body kasRequest: KasRequest
    ): Response<TransaksiDataByIdResponse>

    @GET("/transaksi/{id}")
    suspend fun getTransaksiById(
        @Path("id") id: String
    ): Response<TransaksiDataByIdResponse>

    @PATCH("/transaksi/{id}")
    suspend fun updateTransaksiById(
        @Path("id") id: String,
        @Header("Authorization") jwt: String,
        @Body kasRequest: KasRequest
    ): Response<TransaksiDataByIdResponse>
}