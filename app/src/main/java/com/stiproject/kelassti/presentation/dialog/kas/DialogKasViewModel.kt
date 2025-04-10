package com.stiproject.kelassti.presentation.dialog.kas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stiproject.kelassti.data.model.request.KasRequest
import com.stiproject.kelassti.domain.usecase.GetUserUseCase
import com.stiproject.kelassti.domain.usecase.KasUseCase
import com.stiproject.kelassti.domain.usecase.MahasiswaUseCase
import com.stiproject.kelassti.util.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DialogKasViewModel @Inject constructor(
    private val mahasiswaUseCase: MahasiswaUseCase,
    private val kasUseCase: KasUseCase,
    private val userUseCase: GetUserUseCase
): ViewModel() {

    fun getMahasiswaByName(name: String, onResult: (ApiResult) -> Unit) {
        viewModelScope.launch{
            onResult(mahasiswaUseCase.getMahasiswaByName(name))
        }
    }

    fun getKasDatById(id: Int, onResult: (ApiResult) -> Unit){
        viewModelScope.launch {
            val response = kasUseCase.getKasDataById(id.toString())
            onResult(response)
        }
    }

    fun updateKasById(id: Int, kasRequest: KasRequest, onResult: (ApiResult) -> Unit){
        viewModelScope.launch{
            val response = kasUseCase.updateKasDataById(id.toString(), kasRequest)
            kasUseCase.refreshTriggered()
            onResult(response)
        }
    }

    fun addKasData(kasRequest: KasRequest, onResult: (ApiResult) -> Unit){
        viewModelScope.launch{
            kasUseCase.refreshTriggered()
            onResult(kasUseCase.postKasData(kasRequest))
        }
    }

    fun isUserAdmin(): Boolean {
        return userUseCase.cekUserAdmin()
    }
}