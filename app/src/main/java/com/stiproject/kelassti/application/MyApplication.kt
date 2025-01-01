package com.stiproject.kelassti.application

import android.app.Application
import com.stiproject.kelassti.repository.UserRepository
import com.stiproject.kelassti.viewmodel.state.UserDataHolder
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
    }
}