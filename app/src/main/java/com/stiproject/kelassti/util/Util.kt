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

fun Long.convertToRupiahFormat(): String {

    val x = this.toString().reversed()
    val size = x.length
    val thumb = mutableListOf<String>()

    if (size < 4){
        return this.toString()
    }

    for (i in 0..<size){
        if (i % 3 == 0){
            thumb.add(".")
        }

        thumb.add(x[i].toString())
    }

    return thumb.joinToString("").reversed().dropLast(1)
}

fun Array<String>.acakKelompok(size: Int): MutableList<MutableList<String>> {

    /**
     * @param size Jumlah kelompok
     * @return Setiap index mempresentasikan anggota dari kelompok (String)
     */

    val shuffle = this.toMutableList().shuffled()
    val thum = MutableList(size) { emptyList<String>().toMutableList() }

    for(i in this.indices){
        val y = i % size
        thum[y].add(shuffle[i])
    }

    return thum
}

fun ResponseBody?.parseErrorMessageJsonToString(): String {
    return try {
        val errorResponse = Gson().fromJson(this?.string(), ApiErrorResponse::class.java)
        errorResponse.message
    } catch (e: Exception){
        "Terjadi Kesalahan Saat Memproses Response: ${e.message}" // TODO: mungkin error nya akan terkirim ke server ?
    }
}