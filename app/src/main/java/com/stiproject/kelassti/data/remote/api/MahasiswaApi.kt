package com.stiproject.kelassti.data.remote.api

import com.stiproject.kelassti.data.model.request.AddOrUpdateMahasiswaRequest
import com.stiproject.kelassti.data.model.response.mahasiswa.MahasiswaAllDataResponse
import com.stiproject.kelassti.data.model.response.mahasiswa.MahasiswaDataResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
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
        @Body addOrUpdateMahasiswaRequest: AddOrUpdateMahasiswaRequest
    ): Response<MahasiswaDataResponse>

    @GET("/mahasiswa/{NIM}")
    suspend fun getMahasiswaByNim(
        @Path("NIM") id: String
    ): Response<MahasiswaDataResponse>


    @DELETE("/mahasiswa/{NIM}")
    suspend fun deleteMahasiswa(
        @Header("Authorization") jwt: String,
        @Path("NIM") nim: String
    ): Response<MahasiswaDataResponse>

    @PATCH("/mahasiswa/{NIM}")
    suspend fun updateMahasiswa(
        @Header("Authorization") jwt: String,
        @Path("NIM") nim: String,
        @Body addOrUpdateMahasiswaRequest: AddOrUpdateMahasiswaRequest
    ): Response<MahasiswaDataResponse>
}