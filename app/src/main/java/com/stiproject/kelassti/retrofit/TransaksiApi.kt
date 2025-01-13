package com.stiproject.kelassti.retrofit

import com.stiproject.kelassti.model.request.KasRequest
import com.stiproject.kelassti.model.response.transaksi.TransaksiDataByIdResponse
import com.stiproject.kelassti.model.response.transaksi.TransaksiDataResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TransaksiApi {
    @GET("/transaksi")
    suspend fun getTransaksi(): Response<TransaksiDataResponse>

    @POST("/transaksi")
    suspend fun postTransaksi(
        @Header("Authorization") jwt: String,
        @Body kasRequest: KasRequest
    ): Response<TransaksiDataByIdResponse>

    @GET("/transaksi/{id}")
    suspend fun getTransaksiById(
        @Path("id") id: String
    ): Response<TransaksiDataByIdResponse>

    @PUT("/transaksi/{id}")
    suspend fun updateTransaksiById(
        @Path("id") id: String,
        @Header("Authorization") jwt: String,
        @Body kasRequest: KasRequest
    ): Response<TransaksiDataByIdResponse>
}