package com.stiproject.kelassti.viewmodel

import androidx.lifecycle.ViewModel
import com.stiproject.kelassti.repository.TransaksiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TransaksiViewModel @Inject constructor (val repo: TransaksiRepository): ViewModel() {

}