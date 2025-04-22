package com.stiproject.kelassti.domain.usecase

import com.stiproject.kelassti.data.repository.MahasiswaRepository
import com.stiproject.kelassti.util.ApiResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MahasiswaUseCase @Inject constructor(
    private val repo: MahasiswaRepository
){

    private val _searchQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchMahasiswaByNameResult = _searchQuery
        .flatMapLatest {
            delay(500)
            fetchMahasiswaByName(it)
        }

    fun updateSearchQuery(name: String){
        _searchQuery.value = name
    }

    private suspend fun fetchMahasiswaByName(name: String): Flow<ApiResult> {
        val response = repo.getMahasiswaByName(name)
        val body = response.body()
//            val errBody = response.errorBody()

        if (!response.isSuccessful || body == null){
            return flowOf(ApiResult.Failed("Gagal Mendapatkan Data"))
        }

        return flowOf(ApiResult.Success(body.message, body.data))
    }
}