package com.stiproject.kelassti.presentation.dialog.students

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stiproject.kelassti.data.model.request.AddOrUpdateMahasiswaRequest
import com.stiproject.kelassti.data.model.response.mahasiswa.MahasiswaData
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

    fun initelize(nim: String?){
        if(nim != null){
            getMahasiswaByNim(nim)
        } else {
            _dialogStudentsState.value = DialogStudentsState.GetStudentsSuccess()
        }
    }

    fun addStudents(addOrUpdateMahasiswaRequest: AddOrUpdateMahasiswaRequest){
        viewModelScope.launch{

            val validateData = validateDataUseCase.validateAddMahasiswaRequest(addOrUpdateMahasiswaRequest)

            if (validateData is ValidateDataResult.Success){
                when(val result = mahasiswaUseCase.addMahasiswa(addOrUpdateMahasiswaRequest)){
                    is ApiResult.Failed -> _dialogStudentsState.value = DialogStudentsState.ApiAddOrUpdateStudentsFailed(result.messageFailed)
                    is ApiResult.Success<*> -> _dialogStudentsState.value = DialogStudentsState.ApiAddOrUpdateStudentsSuccess(result.messageSuccess)
                }
            } else {
                _dialogStudentsState.value = DialogStudentsState.ValidationDataFailed((validateData as ValidateDataResult.Failed).message)
            }
        }
    }

    fun getMahasiswaByNim(nim: String){
        viewModelScope.launch{
            when(val result = mahasiswaUseCase.getMahasiswaByNim(nim)){
                is ApiResult.Failed -> _dialogStudentsState.value = DialogStudentsState.GetStudentsFailed(result.messageFailed)
                is ApiResult.Success<*> -> _dialogStudentsState.value = DialogStudentsState.GetStudentsSuccess(result.data as MahasiswaData)
            }
        }
    }

    fun updateStudents(addOrUpdateMahasiswaRequest: AddOrUpdateMahasiswaRequest){
        viewModelScope.launch{
            val validateData = validateDataUseCase.validateAddMahasiswaRequest(addOrUpdateMahasiswaRequest)

            if (validateData is ValidateDataResult.Success){
                when(val result = mahasiswaUseCase.updateMahasiswa(addOrUpdateMahasiswaRequest.NIM.toString(), addOrUpdateMahasiswaRequest)){
                    is ApiResult.Failed -> _dialogStudentsState.value = DialogStudentsState.ApiAddOrUpdateStudentsFailed(result.messageFailed)
                    is ApiResult.Success<*> -> _dialogStudentsState.value = DialogStudentsState.ApiAddOrUpdateStudentsSuccess(result.messageSuccess)
                }
            } else {
                _dialogStudentsState.value = DialogStudentsState.ValidationDataFailed((validateData as ValidateDataResult.Failed).message)
            }
        }
    }

    fun deleteStudents(nim: String){
        viewModelScope.launch{
            when(val result = mahasiswaUseCase.deleteMahasiswa(nim)){
                is ApiResult.Failed -> _dialogStudentsState.value = DialogStudentsState.ApiAddOrUpdateStudentsFailed(result.messageFailed)
                is ApiResult.Success<*> -> _dialogStudentsState.value = DialogStudentsState.ApiAddOrUpdateStudentsSuccess(result.messageSuccess)
            }
        }
    }

    fun setStudentsStateBackToIdle(){
        _dialogStudentsState.value = DialogStudentsState.Idle
    }

    sealed class DialogStudentsState{
        //        object Loading: DialogStudentsState()F
        object Idle: DialogStudentsState()
        data class GetStudentsSuccess(val mahasiswaData: MahasiswaData? = null): DialogStudentsState()
        data class GetStudentsFailed(val message: String): DialogStudentsState()
        data class ApiAddOrUpdateStudentsSuccess(val message: String): DialogStudentsState()
        data class ApiAddOrUpdateStudentsFailed(val message: String): DialogStudentsState()
        data class ValidationDataFailed(val message: String): DialogStudentsState()
    }
}