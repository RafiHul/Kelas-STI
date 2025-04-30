package com.stiproject.kelassti.data.remote.api

import com.stiproject.kelassti.data.model.request.KasRequest
import com.stiproject.kelassti.data.model.response.kas.KasAllDataResponse
import com.stiproject.kelassti.data.model.response.kas.KasDataByIdResponse
import com.stiproject.kelassti.data.model.response.kas.KasDataResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface KasApi {
    @GET("/transaksi/all")
    suspend fun getAllKas(): Response<KasAllDataResponse>


    @GET("/transaksi")
    suspend fun getKasPage(
        @Query("page") page: String,
        @Query("size") size: String
    ): Response<KasDataResponse>

    @POST("/transaksi")
    suspend fun postKas(
        @Header("Authorization") jwt: String,
        @Body kasRequest: KasRequest
    ): Response<KasDataByIdResponse>

    @GET("/transaksi/{id}")
    suspend fun getKasById(
        @Path("id") id: String
    ): Response<KasDataByIdResponse>

    @GET("/transaksi")
    suspend fun getKasByName(
        @Query("name") name: String,
        @Query("page") page: String,
        @Query("size") size: String
    ): Response<KasDataResponse>

    @PATCH("/transaksi/{id}")
    suspend fun updateKasById(
        @Path("id") id: String,
        @Header("Authorization") jwt: String,
        @Body kasRequest: KasRequest
    ): Response<KasDataByIdResponse>
}