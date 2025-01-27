package com.stiproject.kelassti

import android.app.Application
import com.stiproject.kelassti.data.repository.TransaksiRepository
import com.stiproject.kelassti.data.repository.UserRepository
import com.stiproject.kelassti.presentation.state.UserDataHolder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@HiltAndroidApp
class MyApplication: Application() {

    @Module
    @InstallIn(SingletonComponent::class)
    object AppModule{

        @Provides
        @Singleton
        fun provideUserRepository(userDataHolder: UserDataHolder) = UserRepository(userDataHolder)

        @Provides
        @Singleton
        fun provideTransaksiRepository() = TransaksiRepository()
    }
}