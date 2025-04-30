package com.stiproject.kelassti.presentation.ui.kas

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stiproject.kelassti.domain.model.KasSummary
import com.stiproject.kelassti.domain.usecase.GetUserUseCase
import com.stiproject.kelassti.domain.usecase.KasUseCase
import com.stiproject.kelassti.util.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KasViewModel @Inject constructor(
    private val kasUseCase: KasUseCase,
    private val userUseCase: GetUserUseCase
): ViewModel() {

    private val _kasSummary: MutableLiveData<KasSummary> = MutableLiveData()
    val kasSummary = _kasSummary

    private val _kasItemState: MutableLiveData<KasItemState> = MutableLiveData(KasItemState.FetchAll)
    val kasItemState = _kasItemState

    private val _kasPageTriggered = MutableStateFlow(true)

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val getKasPage = _kasPageTriggered
        .flatMapLatest{
            val x = kasItemState.value!!
            when(x){
                is KasItemState.FetchBySearch -> kasUseCase.getKasPageByMahasiswaName(x.query)
                KasItemState.FetchAll -> {
                    kasUseCase.getAllKasData() // TODO: Not Tested if slow
                    _kasSummary.value = kasUseCase.kasSummary
                    kasUseCase.getKasPage()
                }
            }
        }

    fun fetchKasDataByName(query: String){
        _kasItemState.value = KasItemState.FetchBySearch(query)
        refreshKasPageTriggerd()
    }

    fun fetchAllKasData(){
        _kasItemState.value = KasItemState.FetchAll
        refreshKasPageTriggerd()
    }

    fun isUserAdmin(): Boolean {
        return userUseCase.cekUserAdmin()
    }

    private fun refreshKasPageTriggerd(){
        _kasPageTriggered.value = false
        _kasPageTriggered.value = true
    }

    sealed class KasItemState{
        object FetchAll: KasItemState()
        data class FetchBySearch(val query: String): KasItemState()
    }
}