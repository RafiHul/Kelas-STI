package com.stiproject.kelassti.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext val context: Context) {
    private val Context.encryptedDataStore: DataStore<String> by dataStore(
        fileName = "secure_jwt_prefs",
        serializer = EncryptedStringSerializer
    )

    val jwtPreferencesFlow: Flow<String> = context.encryptedDataStore.data

    suspend fun saveJwtPreferences(value: String){
        context.encryptedDataStore.updateData { value }
    }
}