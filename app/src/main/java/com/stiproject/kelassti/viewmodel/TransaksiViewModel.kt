package com.stiproject.kelassti.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stiproject.kelassti.model.response.transaksi.TransaksiDataArray
import com.stiproject.kelassti.repository.TransaksiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransaksiViewModel @Inject constructor (val repo: TransaksiRepository): ViewModel() {
    private val _transaksiKas: MutableLiveData<TransaksiDataArray> = MutableLiveData()
    var transaksiKas = _transaksiKas

    private val _totalTransaksiKas: MutableLiveData<Double> = MutableLiveData()
    val totalTransaksiKas = _totalTransaksiKas

    private val _totalPemasukanKas: MutableLiveData<Double> = MutableLiveData()
    val totalPemasukanKas = _totalPemasukanKas

    private val _totalPengeluaranKas: MutableLiveData<Double> = MutableLiveData()
    val totalPengeluaranKas = _totalPengeluaranKas

    fun getTransaksiKas(action: (String) -> Unit){
        viewModelScope.launch{
            val response = repo.getTransaksi()
            if(response.isSuccessful){
                val body = response.body()
                val dataList = body?.data

                transaksiKas.value = body?.data
                totalPemasukanKas.value = setTotal(dataList,"pemasukan")
                totalPengeluaranKas.value = setTotal(dataList,"pengeluaran")
                totalTransaksiKas.value = dataList?.mapNotNull {
                    it.nominal
                }?.reduce { acc, nominal ->
                    acc + nominal
                }

                action(body?.message.toString())
            } else {
                action(response.code().toString())
            }
        }
    }


    private fun setTotal(data: TransaksiDataArray?, type: String): Double? {
        val result = data?.filter {
            it.type == type
        }?.mapNotNull {
            it.nominal
        }?.reduce { acc, nominal ->
            acc + nominal
        }

        return result
    }

}