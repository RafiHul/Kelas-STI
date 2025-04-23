package com.stiproject.kelassti.data.remote.api

import com.stiproject.kelassti.data.model.request.AddMahasiswaRequest
import com.stiproject.kelassti.data.model.request.KasRequest
import com.stiproject.kelassti.data.model.response.mahasiswa.MahasiswaAllDataResponse
import com.stiproject.kelassti.data.model.response.mahasiswa.MahasiswaDataResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MahasiswaApi {

    @GET("/mahasiswa")
    suspend fun getMahasiswaByName(
        @Query("name") name: String
    ): Response<MahasiswaAllDataResponse>

    @GET("/mahasiswa")
    suspend fun getAllMahasiswa(): Response<MahasiswaAllDataResponse>

    @POST("/mahasiswa")
    suspend fun addMahasiswa(
        @Header("Authorization") jwt: String,
        @Body addMahasiswaRequest: AddMahasiswaRequest
    ): Response<MahasiswaDataResponse>

    @GET("/mahasiswa/{id}")
    suspend fun getMahasiswaByNim(
        @Path("id") id: String
    ): Response<MahasiswaDataResponse>
}