package com.stiproject.kelassti.presentation.dialog.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stiproject.kelassti.data.model.request.TasksRequest
import com.stiproject.kelassti.data.model.response.dosen.DosenDataArray
import com.stiproject.kelassti.data.model.response.transaksi.TransaksiData
import com.stiproject.kelassti.domain.model.ValidateDataResult
import com.stiproject.kelassti.domain.usecase.DosenUseCase
import com.stiproject.kelassti.domain.usecase.TasksUseCase
import com.stiproject.kelassti.domain.usecase.ValidateDataUseCase
import com.stiproject.kelassti.util.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DialogHomeViewModel @Inject constructor(
    private val tasksUseCase: TasksUseCase,
    private val dosenUseCase: DosenUseCase,
    private val validateDataUseCase: ValidateDataUseCase
): ViewModel() {

    private val _dialogHomeState = MutableLiveData<DialogHomeState>(DialogHomeState.Idle)
    val dialogHomeState = _dialogHomeState

    private val _dosenState = MutableLiveData<DosenDataArray?>()
    val dosenState = _dosenState

    fun createTasks(tasksRequest: TasksRequest){
        viewModelScope.launch{

            val validateData = validateDataUseCase.validateTasksRequest(tasksRequest)

            if (validateData is ValidateDataResult.Success){
                when(val result = tasksUseCase.createTasks(tasksRequest)){
                    is ApiResult.Failed -> _dialogHomeState.postValue(DialogHomeState.ApiFailed(result.messageFailed))
                    is ApiResult.Success<*> -> _dialogHomeState.postValue(DialogHomeState.ApiPostTasksSuccess(result.messageSuccess))
                }
            } else {
                _dialogHomeState.postValue(DialogHomeState.ValidationDataFailed((validateData as ValidateDataResult.Failed).message))
            }
        }
    }

    fun getAllDosen(){
        viewModelScope.launch{
            when(val result = dosenUseCase.getDosen()){
                is ApiResult.Failed -> _dialogHomeState.postValue(DialogHomeState.ApiFailed(result.messageFailed))
                is ApiResult.Success<*> -> _dosenState.postValue(result.data as DosenDataArray?)
            }
        }
    }

    fun setHomeStateBackToIdle(){
        _dialogHomeState.postValue(DialogHomeState.Idle)
    }

    sealed class DialogHomeState{
        //        object Loading: DialogHomeState()
        object Idle: DialogHomeState()
        data class ApiGetTasksSuccess(val data: TransaksiData? = null): DialogHomeState()
        data class ApiPostTasksSuccess(val message: String): DialogHomeState()
        data class ApiFailed(val message: String): DialogHomeState()
        data class ValidationDataFailed(val message: String): DialogHomeState()
    }
}