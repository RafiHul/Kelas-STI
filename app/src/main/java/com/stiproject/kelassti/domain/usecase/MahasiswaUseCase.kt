package com.stiproject.kelassti.domain.usecase

import android.util.Log
import com.stiproject.kelassti.data.local.JwtTokenStorage
import com.stiproject.kelassti.data.model.request.AddOrUpdateMahasiswaRequest
import com.stiproject.kelassti.data.repository.MahasiswaRepository
import com.stiproject.kelassti.util.ApiResult
import com.stiproject.kelassti.util.parseErrorMessageJsonToString
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
    private val repo: MahasiswaRepository,
    private val jwtTokenStorage: JwtTokenStorage
){

    private val jwtBearer = jwtTokenStorage.getJwtBearer()
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

    suspend fun getAllMahasiswa(): ApiResult {
        val response = repo.getAllMahasiswa()
        val body = response.body()
        val errorBody = response.errorBody()

        if(!response.isSuccessful){
            return ApiResult.Failed(errorBody.parseErrorMessageJsonToString())
        }

        if (body == null){
            return ApiResult.Failed("Gagal Memuat data mahasiswa")
        }

        return ApiResult.Success(body.message, body.data)
    }

    suspend fun getMahasiswaByNim(nim: String): ApiResult {
        val response = repo.getMahasiswaByNim(nim)
        val body = response.body()
        val errorBody = response.errorBody()

        if(!response.isSuccessful){
            return ApiResult.Failed(errorBody.parseErrorMessageJsonToString())
        }

        if (body == null){
            return ApiResult.Failed("Gagal Memuat data mahasiswa")
        }

        return ApiResult.Success(body.message, body.data)
    }

    suspend fun addMahasiswa(addOrUpdateMahasiswaRequest: AddOrUpdateMahasiswaRequest): ApiResult {
        val response = repo.addMahasiswa(jwtBearer, addOrUpdateMahasiswaRequest)
        val body = response.body()
        val errBody = response.errorBody()

        if(!response.isSuccessful){
            return ApiResult.Failed(errBody.parseErrorMessageJsonToString())
        }

        if (body == null){
            return ApiResult.Failed("Gagal Menambahkan data")
        }

        return ApiResult.Success("Berhasil menambahkan data", null)
    }

    suspend fun deleteMahasiswa(nim: String): ApiResult {
        val response = repo.deleteMahasiswa(jwtBearer, nim)
        val errBody = response.errorBody()

        if(!response.isSuccessful){
            return ApiResult.Failed(errBody.parseErrorMessageJsonToString())
        }

        return ApiResult.Success("Berhasil menghapus data", null)
    }

    suspend fun updateMahasiswa(nim: String, addOrUpdateMahasiswaRequest: AddOrUpdateMahasiswaRequest): ApiResult {
        val response = repo.updateMahasiswa(jwtBearer, nim, addOrUpdateMahasiswaRequest)
        val body = response.body()
        val errBody = response.errorBody()

        if(!response.isSuccessful){
            return ApiResult.Failed(errBody.parseErrorMessageJsonToString())
        }

        if(body == null){
            return ApiResult.Failed("Gagal mengupdate data")
        }

        return ApiResult.Success("Berhasil mengupdate data", null)
    }
}