package com.stiproject.kelassti.util

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import com.stiproject.kelassti.model.response.ApiErrorResponse
import okhttp3.ResponseBody

fun handleToastApiResult(context: Context?, result: ApiResult<String>){
    when(result){
        is ApiResult.Failed -> Toast.makeText(context, result.messageFailed, Toast.LENGTH_SHORT).show()
        is ApiResult.Success -> Toast.makeText(context, result.messageSuccess, Toast.LENGTH_SHORT).show()
    }
}

fun ResponseBody?.parseErrorMessageJsonToString(): String {
    return try {
        val errorResponse = Gson().fromJson(this?.string(), ApiErrorResponse::class.java)
        errorResponse.message
    } catch (e: Exception){
        "Terjadi Kesalahan Saat Memproses Response: ${e.message}" // TODO: mungkin error nya akan terkirim ke server ?
    }
}