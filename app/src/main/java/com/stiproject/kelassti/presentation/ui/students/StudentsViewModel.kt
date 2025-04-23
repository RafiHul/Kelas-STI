package com.stiproject.kelassti.presentation.ui.students

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stiproject.kelassti.data.model.response.mahasiswa.MahasiswaDataArray
import com.stiproject.kelassti.domain.usecase.MahasiswaUseCase
import com.stiproject.kelassti.domain.usecase.ValidateDataUseCase
import com.stiproject.kelassti.util.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentsViewModel @Inject constructor(
    private val mahasiswaUseCase: MahasiswaUseCase,
    private val validateDataUseCase: ValidateDataUseCase
): ViewModel() {

    private val _mahasiswaState: MutableLiveData<MahasiswaState> = MutableLiveData(MahasiswaState.Idle)
    val mahasiswaState = _mahasiswaState

    fun getAllMahasiswa(){
        viewModelScope.launch{
            when(val result = mahasiswaUseCase.getAllMahasiswa()){
                is ApiResult.Failed -> _mahasiswaState.postValue(MahasiswaState.FailedGetData(result.messageFailed))
                is ApiResult.Success<*> -> _mahasiswaState.postValue(MahasiswaState.SuccessGetData(result.data as MahasiswaDataArray?))
            }
        }
    }

    sealed class MahasiswaState{
        object Idle: MahasiswaState()
        data class FailedGetData(val message: String): MahasiswaState()
        data class SuccessGetData(val data: MahasiswaDataArray?): MahasiswaState()
    }
}