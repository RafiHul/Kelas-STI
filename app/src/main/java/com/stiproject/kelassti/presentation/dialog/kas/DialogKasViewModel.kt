package com.stiproject.kelassti.presentation.dialog.kas

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stiproject.kelassti.data.model.request.KasRequest
import com.stiproject.kelassti.data.model.response.kas.KasData
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

    private val _dialogKasState = MutableLiveData<DialogKasState>(DialogKasState.Idle)
    val dialogKasState = _dialogKasState

    fun initialize(kasId: Int?){
        if (kasId != null){
            getKasDataById(kasId.toString())
        } else {
            _dialogKasState.postValue(DialogKasState.ApiGetSuccess())
        }

    }

    fun getMahasiswaByName(name: String, onResult: (ApiResult) -> Unit) {
        viewModelScope.launch{
            onResult(mahasiswaUseCase.getMahasiswaByName(name))
        }
    }

    fun getKasDataById(id: String){
        viewModelScope.launch {
            when (val result = kasUseCase.getKasDataById(id)) {
                is ApiResult.Failed -> _dialogKasState.postValue(DialogKasState.ApiFailed(result.messageFailed))
                is ApiResult.Success<*> -> _dialogKasState.postValue(DialogKasState.ApiGetSuccess(result.data as KasData))
            }
        }
    }

    fun updateKasById(id: Int, kasRequest: KasRequest, callback: () -> Unit){
        viewModelScope.launch{

            val validateData = validateDataUseCase.validateKasRequest(kasRequest)

            if (validateData is ValidateDataResult.Success){
                when(val result = kasUseCase.updateKasDataById(id.toString(), kasRequest)){
                    is ApiResult.Failed -> _dialogKasState.postValue(DialogKasState.ApiFailed(result.messageFailed))
                    is ApiResult.Success<*> -> {
                        callback()
                        _dialogKasState.postValue(DialogKasState.ApiPostSuccess(result.messageSuccess))
                    }
                }
            } else {
                _dialogKasState.postValue(DialogKasState.ValidationDataFailed((validateData as ValidateDataResult.Failed).message))
            }
        }
    }

    fun addKasData(kasRequest: KasRequest, callback: () -> Unit){
        viewModelScope.launch{

            val validateData = validateDataUseCase.validateKasRequest(kasRequest)

            if (validateData is ValidateDataResult.Success){
                when(val result = kasUseCase.postKasData(kasRequest)){
                    is ApiResult.Failed -> _dialogKasState.postValue(DialogKasState.ApiFailed(result.messageFailed))
                    is ApiResult.Success<*> -> {
                        callback()
                        _dialogKasState.postValue(DialogKasState.ApiPostSuccess(result.messageSuccess))
                    }
                }
            } else {
                _dialogKasState.postValue(DialogKasState.ValidationDataFailed((validateData as ValidateDataResult.Failed).message))
            }

        }
    }

    fun isUserAdmin(): Boolean {
        return userUseCase.cekUserAdmin()
    }

    fun setAddOrUpdateStateBackToIdle(){
        _dialogKasState.postValue(DialogKasState.Idle)
    }

    sealed class DialogKasState{
//        object Loading: DialogKasState()
        object Idle: DialogKasState()
        data class ApiGetSuccess(val data: KasData? = null): DialogKasState()
        data class ApiPostSuccess(val message: String): DialogKasState()
        data class ApiFailed(val message: String): DialogKasState()
        data class ValidationDataFailed(val message: String): DialogKasState()
    }
}