package com.stiproject.kelassti.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val baseUrl = "http://192.168.1.5:3000"
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val getUserService: UserApi by lazy {
        retrofit.create(UserApi::class.java)
    }
}