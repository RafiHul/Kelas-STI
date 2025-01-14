package com.stiproject.kelassti.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stiproject.kelassti.model.request.KasRequest
import com.stiproject.kelassti.model.response.transaksi.TransaksiData
import com.stiproject.kelassti.model.response.transaksi.TransaksiDataArray
import com.stiproject.kelassti.repository.TransaksiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransaksiViewModel @Inject constructor (val repo: TransaksiRepository): ViewModel() {
    private val _transaksiKas: MutableLiveData<TransaksiDataArray> = MutableLiveData()
    var transaksiKas = _transaksiKas

    private val _totalTransaksiKas: MutableLiveData<Long> = MutableLiveData()
    val totalTransaksiKas = _totalTransaksiKas

    private val _totalPemasukanKas: MutableLiveData<Long> = MutableLiveData()
    val totalPemasukanKas = _totalPemasukanKas

    private val _totalPengeluaranKas: MutableLiveData<Long> = MutableLiveData()
    val totalPengeluaranKas = _totalPengeluaranKas

    fun getTransaksiKas(action: (String) -> Unit = {}){
        viewModelScope.launch{
            val response = repo.getAllTransaksi()
            if(response.isSuccessful){
                val body = response.body()
                val dataList = body?.data

                transaksiKas.value = body?.data

                val pemasukanKas = setTotal(dataList,"pemasukan")
                val pengeluaranKas = setTotal(dataList,"pengeluaran")
                val totalKas = pemasukanKas!! - pengeluaranKas!!

                totalPemasukanKas.value = pemasukanKas
                totalPengeluaranKas.value = pengeluaranKas
                totalTransaksiKas.value = totalKas

                action(body?.message.toString())
            } else {
                action(response.code().toString())
            }
        }
    }

    fun addTransaksiKas(jwtToken: String, kasRequest: KasRequest, action: (String) -> Unit){
        // TODO: ini belum di tambahkan kalo misalnya bukan admin
        viewModelScope.launch{
            val response = repo.postTransaksi(jwtToken,kasRequest)
            val body = response.body()

            if(response.code() == 404){ // TODO: ini ubah jangan di if dari response code nya
                action("NIM tidak ditemukan!")
                return@launch
            }

            if (response.isSuccessful && body != null){
                getTransaksiKas()
                action(body.message)
            } else {
                action(response.message())
            }
        }
    }

    fun getTransaksiById(id: Int, action: (TransaksiData?) -> Unit) {
        viewModelScope.launch {
            val response = repo.getTransaksiById(id.toString())
            val body = response.body()

            if (!response.isSuccessful || body == null) {
                action(null)
                return@launch
            }

            action(body.data)
        }
    }

    fun updateTransaksiById(id: Int, jwtToken: String, kasRequest: KasRequest, action: (String) -> Unit){
        viewModelScope.launch{
            val response = repo.updateTransaksiById(id.toString(), jwtToken, kasRequest)
            val body = response.body()

            if(!response.isSuccessful || body == null){
                action("Gagal update data")
                return@launch
            }

            getTransaksiKas()
            action(body.message.toString())
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