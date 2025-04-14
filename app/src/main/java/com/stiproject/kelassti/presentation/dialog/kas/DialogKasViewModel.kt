package com.stiproject.kelassti.presentation.dialog.kas

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stiproject.kelassti.data.model.request.KasRequest
import com.stiproject.kelassti.domain.model.DialogBehaviour
import com.stiproject.kelassti.domain.model.ValidateDataResult
import com.stiproject.kelassti.domain.usecase.GetUserUseCase
import com.stiproject.kelassti.domain.usecase.KasUseCase
import com.stiproject.kelassti.domain.usecase.MahasiswaUseCase
import com.stiproject.kelassti.domain.usecase.ValidateDataUseCase
import com.stiproject.kelassti.util.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DialogKasViewModel @Inject constructor(
    private val mahasiswaUseCase: MahasiswaUseCase,
    private val kasUseCase: KasUseCase,
    private val userUseCase: GetUserUseCase,
    private val validateDataUseCase: ValidateDataUseCase
): ViewModel() {

    private val _dialogBehaviour = MutableLiveData<DialogBehaviour>(DialogBehaviour.ADDNEW)
    val dialogBehaviour = _dialogBehaviour

    private val _apiResult = MutableLiveData<ApiResult>()
    val apiResult = _apiResult


    fun setDialogBehaviour(x: DialogBehaviour){
        _dialogBehaviour.postValue(x)
    }

    fun getMahasiswaByName(name: String, onResult: (ApiResult) -> Unit) {
        viewModelScope.launch{
            onResult(mahasiswaUseCase.getMahasiswaByName(name))
        }
    }

    fun getKasDataById(id: Int, onResult: (ApiResult) -> Unit){
        viewModelScope.launch {
            val response = kasUseCase.getKasDataById(id.toString())
            onResult(response)
        }
    }

    fun updateKasById(id: Int, kasRequest: KasRequest, onValidationError: (ValidateDataResult.Failed) -> Unit){
        viewModelScope.launch{
            val validateData = validateDataUseCase.validateKasRequest(kasRequest)

            if(validateData is ValidateDataResult.Success){
                _apiResult.postValue(kasUseCase.updateKasDataById(id.toString(), kasRequest))
            } else {
                onValidationError(validateData as ValidateDataResult.Failed)
            }
        }
    }

    fun addKasData(kasRequest: KasRequest, onValidationError: (ValidateDataResult.Failed) -> Unit){
        viewModelScope.launch{
            val validateData = validateDataUseCase.validateKasRequest(kasRequest)

            if(validateData is ValidateDataResult.Success){
                _apiResult.postValue(kasUseCase.postKasData(kasRequest))
            } else {
                onValidationError(validateData as ValidateDataResult.Failed)
            }
        }
    }

    fun isUserAdmin(): Boolean {
        return userUseCase.cekUserAdmin()
    }
}