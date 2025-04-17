package com.stiproject.kelassti.presentation.ui.kas

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.stiproject.kelassti.data.model.request.KasRequest
import com.stiproject.kelassti.domain.model.KasSummary
import com.stiproject.kelassti.domain.usecase.GetUserUseCase
import com.stiproject.kelassti.domain.usecase.KasUseCase
import com.stiproject.kelassti.util.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KasViewModel @Inject constructor(
    private val kasUseCase: KasUseCase,
    private val userUseCase: GetUserUseCase
): ViewModel() {

    private val _kasSummary: MutableLiveData<KasSummary> = MutableLiveData()
    val kasSummary = _kasSummary

    val getKasPage = kasUseCase.getTransaksiPage
        .cachedIn(viewModelScope)

    fun getKas(onResult: (ApiResult) -> Unit) {
        viewModelScope.launch {
            onResult(kasUseCase.getAllKasData())
            _kasSummary.postValue(kasUseCase.kasSummary)
        }
    }

    fun refreshKasSummary(){
        _kasSummary.postValue(kasUseCase.kasSummary)
    }

    fun isUserAdmin(): Boolean {
        return userUseCase.cekUserAdmin()
    }
}