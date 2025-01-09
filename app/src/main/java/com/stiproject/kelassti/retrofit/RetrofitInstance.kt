package com.stiproject.kelassti.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val baseUrl = "http://node1.eclipsegate.my.id:8300"
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val getUserService: UserApi by lazy {
        retrofit.create(UserApi::class.java)
    }

    val getTransaksiService: TransaksiApi by lazy {
        retrofit.create(TransaksiApi::class.java)
    }
}