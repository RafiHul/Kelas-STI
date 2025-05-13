package com.stiproject.kelassti.presentation.dialog.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stiproject.kelassti.data.model.request.TasksRequest
import com.stiproject.kelassti.data.model.response.dosen.DosenDataArray
import com.stiproject.kelassti.data.model.response.tasks.TasksData
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

    private val _dialogHomeState = MutableLiveData<DialogHomeState>(DialogHomeState.Loading)
    val dialogHomeState = _dialogHomeState

    private val _dosenState = MutableLiveData<DosenDataArray?>()
    val dosenState = _dosenState

    fun initelize(tasksId: Int?){
        if (tasksId == null){
            _dialogHomeState.value = DialogHomeState.ApiGetTasksSuccess()
        } else {
            getTasksById(tasksId)
        }
    }

    fun createTasks(tasksRequest: TasksRequest){
        viewModelScope.launch{

            _dialogHomeState.value = DialogHomeState.Loading
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

    fun getTasksById(id: Int){
        viewModelScope.launch{
            when(val result = tasksUseCase.getTasksById(id)){
                is ApiResult.Failed -> _dialogHomeState.postValue(DialogHomeState.ApiFailed(result.messageFailed))
                is ApiResult.Success<*> -> _dialogHomeState.postValue(DialogHomeState.ApiGetTasksSuccess(result.data as TasksData))
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

    fun updateTasksById(tasksId: Int, tasksRequest: TasksRequest){
        viewModelScope.launch{
            _dialogHomeState.value = DialogHomeState.Loading
            val validateData = validateDataUseCase.validateTasksRequest(tasksRequest)

            if (validateData is ValidateDataResult.Success){
                when(val result = tasksUseCase.updateTasksById(tasksId, tasksRequest)){
                    is ApiResult.Failed -> _dialogHomeState.value = DialogHomeState.ApiFailed(result.messageFailed)
                    is ApiResult.Success<*> -> _dialogHomeState.value = DialogHomeState.ApiPostTasksSuccess(result.messageSuccess)
                }
            } else {
                _dialogHomeState.value = DialogHomeState.ValidationDataFailed((validateData as ValidateDataResult.Failed).message)
            }
        }
    }

    fun setHomeStateBackToLoading(){
        _dialogHomeState.postValue(DialogHomeState.Loading)
    }

    sealed class DialogHomeState{
        object Loading: DialogHomeState()
        data class ApiGetTasksSuccess(val data: TasksData? = null): DialogHomeState()
        data class ApiPostTasksSuccess(val message: String): DialogHomeState()
        data class ApiFailed(val message: String): DialogHomeState()
        data class ValidationDataFailed(val message: String): DialogHomeState()
    }
}