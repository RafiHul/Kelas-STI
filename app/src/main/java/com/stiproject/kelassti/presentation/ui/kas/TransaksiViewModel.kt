package com.stiproject.kelassti.presentation.ui.kas

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.stiproject.kelassti.data.model.request.KasRequest
import com.stiproject.kelassti.data.model.response.transaksi.TransaksiData
import com.stiproject.kelassti.data.model.response.transaksi.TransaksiDataArray
import com.stiproject.kelassti.data.repository.TransaksiRepository
import com.stiproject.kelassti.util.ApiResult
import com.stiproject.kelassti.util.parseErrorMessageJsonToString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransaksiViewModel @Inject constructor (val repo: TransaksiRepository): ViewModel() {
//    private val _transaksiKas: MutableLiveData<TransaksiDataArray> = MutableLiveData()
//    var transaksiKas = _transaksiKas

    private val _totalTransaksiKas: MutableLiveData<Long> = MutableLiveData()
    val totalTransaksiKas = _totalTransaksiKas

    private val _totalPemasukanKas: MutableLiveData<Long> = MutableLiveData()
    val totalPemasukanKas = _totalPemasukanKas

    private val _totalPengeluaranKas: MutableLiveData<Long> = MutableLiveData()
    val totalPengeluaranKas = _totalPengeluaranKas

    val _refreshTrigger = MutableStateFlow(true)

    @OptIn(ExperimentalCoroutinesApi::class)
    val getTransaksiPage = _refreshTrigger
        .flatMapLatest { repo.getTransaksiPage() }
        .cachedIn(viewModelScope)

    fun refreshTriggered(){
        _refreshTrigger.value = false
        _refreshTrigger.value = true
    }

    fun getTransaksi(action: (ApiResult<String>) -> Unit = {}){
        viewModelScope.launch {

            val response = repo.getAllTransaksi()
            val body = response.body()
            val errorBody = response.errorBody()

            if(!response.isSuccessful){
                action(ApiResult.Failed(errorBody.parseErrorMessageJsonToString()))
                return@launch
            }

            if (body == null){
                action(ApiResult.Failed("Gagal Mendapatkan data kas"))
                return@launch
            }

            val dataList = body.data

            val pemasukanKas = setTotal(dataList,"pemasukan")
            val pengeluaranKas = setTotal(dataList,"pengeluaran")
            val totalKas = pemasukanKas!! - pengeluaranKas!!

            _totalPemasukanKas.postValue(pemasukanKas)
            _totalPengeluaranKas.postValue(pengeluaranKas)
            _totalTransaksiKas.postValue(totalKas)
        }
    }

    fun addTransaksiKas(jwtToken: String, kasRequest: KasRequest, action: (ApiResult<String>) -> Unit){
        viewModelScope.launch{

            val response = repo.postTransaksi(jwtToken,kasRequest)
            val body = response.body()
            val errorBody = response.errorBody()

            if(!response.isSuccessful){
                action(ApiResult.Failed(errorBody.parseErrorMessageJsonToString()))
                return@launch
            }

            if (body == null){
                action(ApiResult.Failed("Gagal Menambahkan Data"))
                return@launch
            }

            getTransaksi()
            refreshTriggered()
            action(ApiResult.Success(body.message))
        }
    }

    fun getTransaksiById(id: Int, action: (ApiResult<TransaksiData?>) -> Unit) {
        viewModelScope.launch {

            val response = repo.getTransaksiById(id.toString())
            val body = response.body()

            if (!response.isSuccessful || body == null) {
                action(ApiResult.Failed(null)) // TODO: ini bisa pake body.message (string)
                return@launch
            }

            action(ApiResult.Success(body.data))
        }
    }

    fun updateTransaksiById(id: Int, jwtToken: String, kasRequest: KasRequest, action: (ApiResult<String>) -> Unit){
        viewModelScope.launch{

            val response = repo.updateTransaksiById(id.toString(), jwtToken, kasRequest)
            val body = response.body()
            val errorBody = response.errorBody()

            if(!response.isSuccessful){
                action(ApiResult.Failed(errorBody.parseErrorMessageJsonToString()))
                return@launch
            }

            getTransaksi()
            refreshTriggered()
            action(ApiResult.Success(body?.message.toString()))
        }
    }


    private fun setTotal(data: TransaksiDataArray?, type: String): Long? {
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