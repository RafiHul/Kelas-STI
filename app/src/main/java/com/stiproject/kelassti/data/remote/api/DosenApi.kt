package com.stiproject.kelassti.data.remote.api

import com.stiproject.kelassti.data.model.response.dosen.DosenDataResponse
import retrofit2.Response
import retrofit2.http.GET

interface DosenApi {

    @GET("/dosens")
    suspend fun getDosen(): Response<DosenDataResponse>
}