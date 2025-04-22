package com.stiproject.kelassti.data.remote

import com.stiproject.kelassti.data.remote.api.DosenApi
import com.stiproject.kelassti.data.remote.api.TasksApi
import com.stiproject.kelassti.data.remote.api.KasApi
import com.stiproject.kelassti.data.remote.api.UserApi
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

    val getTransaksiService: KasApi by lazy {
        retrofit.create(KasApi::class.java)
    }

    val getTasksService: TasksApi by lazy {
        retrofit.create(TasksApi::class.java)
    }

    val getDosenService: DosenApi by lazy {
        retrofit.create(DosenApi::class.java)
    }
}