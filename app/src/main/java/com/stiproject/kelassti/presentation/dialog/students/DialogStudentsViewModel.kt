package com.stiproject.kelassti.presentation.dialog.students

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stiproject.kelassti.data.model.request.AddMahasiswaRequest
import com.stiproject.kelassti.data.model.response.kas.KasData
import com.stiproject.kelassti.domain.model.ValidateDataResult
import com.stiproject.kelassti.domain.usecase.MahasiswaUseCase
import com.stiproject.kelassti.domain.usecase.ValidateDataUseCase
import com.stiproject.kelassti.util.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DialogStudentsViewModel @Inject constructor(
    private val mahasiswaUseCase: MahasiswaUseCase,
    private val validateDataUseCase: ValidateDataUseCase
): ViewModel() {

    private val _dialogStudentsState = MutableLiveData<DialogStudentsState>(DialogStudentsState.Idle)
    val dialogStudentsState = _dialogStudentsState

    fun addStudents(addMahasiswaRequest: AddMahasiswaRequest){
        viewModelScope.launch{

            val validateData = validateDataUseCase.validateAddMahasiswaRequest(addMahasiswaRequest)

            if (validateData is ValidateDataResult.Success){
                when(val result = mahasiswaUseCase.addMahasiswa(addMahasiswaRequest)){
                    is ApiResult.Failed -> _dialogStudentsState.postValue(DialogStudentsState.ApiAddStudentsFailed(result.messageFailed))
                    is ApiResult.Success<*> -> _dialogStudentsState.postValue(DialogStudentsState.ApiAddStudentsSuccess(result.messageSuccess))
                }
            } else {
                _dialogStudentsState.postValue(DialogStudentsState.ValidationDataFailed((validateData as ValidateDataResult.Failed).message))
            }
        }
    }

    fun setStudentsStateBackToIdle(){
        _dialogStudentsState.postValue(DialogStudentsState.Idle)
    }

    sealed class DialogStudentsState{
        //        object Loading: DialogStudentsState()
        object Idle: DialogStudentsState()
        data class ApiGetTasksSuccess(val data: KasData? = null): DialogStudentsState()
        data class ApiAddStudentsSuccess(val message: String): DialogStudentsState()
        data class ApiAddStudentsFailed(val message: String): DialogStudentsState()
        data class ValidationDataFailed(val message: String): DialogStudentsState()
    }
}