package com.stiproject.kelassti.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.stiproject.kelassti.data.local.JwtTokenStorage
import com.stiproject.kelassti.data.model.request.KasRequest
import com.stiproject.kelassti.data.model.response.kas.KasData
import com.stiproject.kelassti.data.model.response.kas.KasDataArray
import com.stiproject.kelassti.data.remote.paging.KasByNamePagingSource
import com.stiproject.kelassti.data.remote.paging.KasPagingSource
import com.stiproject.kelassti.data.repository.KasRepository
import com.stiproject.kelassti.domain.model.KasSummary
import com.stiproject.kelassti.util.ApiResult
import com.stiproject.kelassti.util.parseErrorMessageJsonToString
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.isNotEmpty
import kotlin.collections.map

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@Singleton
class KasUseCase @Inject constructor(
    private val repo: KasRepository,
    private val jwtTokenStorage: JwtTokenStorage
){

    private val userJwtToken = jwtTokenStorage.getJwtBearer()
    val kasSummary = KasSummary(0, 0, 0)

    fun getKasPage(): Flow<PagingData<KasData>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { KasPagingSource() }
        ).flow
    }

    fun getKasPageByMahasiswaName(name: String): Flow<PagingData<KasData>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { KasByNamePagingSource(name) }
        ).flow
    }

    suspend fun getAllKasData(): ApiResult {
        val response = repo.getAllKasData()
        val body = response.body()
        val errorBody = response.errorBody()

        if(!response.isSuccessful){
            return ApiResult.Failed(errorBody.parseErrorMessageJsonToString())
        }

        if (body == null){
            return ApiResult.Failed("Gagal Mendapatkan data kas")
        }

        setKasSummary(body.data)
        return ApiResult.Success(body.message, body.data)
    }

    fun setKasSummary(data: KasDataArray){
        val pemasukan = calcTotal(data, "pemasukan")!!
        val pengeluaran = calcTotal(data, "pengeluaran")!!
        val total = pemasukan - pengeluaran

        kasSummary.pemasukan = pemasukan
        kasSummary.pengeluaran = pengeluaran
        kasSummary.total = total
    }

    suspend fun postKasData(kasRequest: KasRequest): ApiResult {
        val response = repo.addKasData(userJwtToken, kasRequest)
        val body = response.body()
        val errorBody = response.errorBody()

        if(!response.isSuccessful){
            return ApiResult.Failed(errorBody.parseErrorMessageJsonToString())
        }

        if (body == null){
            return ApiResult.Failed("Gagal Menambahkan Data")
        }


        getAllKasData()
        return ApiResult.Success(body.message, null)
    }

    suspend fun getKasDataById(id: String): ApiResult {
        val response = repo.getKasDataById(id)
        val body = response.body()

        if (!response.isSuccessful || body == null) {
            return ApiResult.Failed(response.errorBody().parseErrorMessageJsonToString())
        }

        return ApiResult.Success(body.message, body.data)
    }

    suspend fun updateKasDataById(id: String, kasRequest: KasRequest): ApiResult {
        val response = repo.updateKasDataById(id, userJwtToken, kasRequest)
        val body = response.body()
        val errorBody = response.errorBody()

        if(!response.isSuccessful || body == null){
            return ApiResult.Failed(errorBody.parseErrorMessageJsonToString())
        }

        getAllKasData()
        return ApiResult.Success(body.message,body.data)
    }

    private fun calcTotal(data: KasDataArray?, type: String): Long? {
        var result1 = data?.filter {
            it.type == type
        }

        if (result1 != null && result1.isNotEmpty()){
            var result = result1.map{
                it.nominal
            }.reduce { acc, nominal ->
                acc + nominal
            }
            return result.toLong()
        }
        return 0
    }
}