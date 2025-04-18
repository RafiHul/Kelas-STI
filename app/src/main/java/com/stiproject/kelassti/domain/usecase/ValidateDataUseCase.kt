package com.stiproject.kelassti.domain.usecase

import com.stiproject.kelassti.data.model.request.KasRequest
import com.stiproject.kelassti.data.model.request.TasksRequest
import com.stiproject.kelassti.domain.model.ValidateDataResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ValidateDataUseCase @Inject constructor(){

    fun validateKasRequest(kasRequest: KasRequest): ValidateDataResult {
        if (kasRequest.NIM_mahasiswa == 0){
            return ValidateDataResult.Failed("Nim tidak boleh kosong")
        }

        if(kasRequest.nominal == 0){
            return ValidateDataResult.Failed("Nominal tidak boleh kosong")
        }

        if(kasRequest.deskripsi == ""){
            return ValidateDataResult.Failed("Harap tambahkan deskripsi transaksi")
        }

        if(kasRequest.type == ""){
            return ValidateDataResult.Failed("Masukkan tipe transaksi")
        }

        return ValidateDataResult.Success
    }

    fun validateTasksRequest(tasksRequest: TasksRequest): ValidateDataResult {

        if(tasksRequest.dosenId == 0){
            return ValidateDataResult.Failed("dosen tidak boleh kosong")
        }

        if(tasksRequest.description == ""){
            return ValidateDataResult.Failed("deskripsi tidak boleh kosong")
        }

        if(tasksRequest.deadline == ""){
            return ValidateDataResult.Failed("harap pilih waktu deadline tugas")
        }

        if(tasksRequest.title == ""){
            return ValidateDataResult.Failed("masukkan title")
        }

        return ValidateDataResult.Success
    }

}