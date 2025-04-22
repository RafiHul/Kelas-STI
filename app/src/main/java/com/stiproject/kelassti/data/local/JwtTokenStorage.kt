package com.stiproject.kelassti.data.local

import android.content.Context
import com.stiproject.kelassti.presentation.state.UserDataHolder
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JwtTokenStorage @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userState: UserDataHolder
) {
    suspend fun setJwtToken(token: String){
        userState.setJwtToken(token)
        DataStoreUtil.saveLoginToken(context,token)
    }
    suspend fun clearJwtToken(){
        DataStoreUtil.clearLoginInfo(context)
    }

    suspend fun getLoginTokenObserve(onResult: (String) -> Unit){
        DataStoreUtil.getLoginToken(context).collect{
            setJwtToken(it)
            onResult(it)
        }
    }

    fun getJwt() = userState.userState.value?.jwtToken

    fun getJwtBearer() = "Bearer ${userState.userState.value?.jwtToken}"
}