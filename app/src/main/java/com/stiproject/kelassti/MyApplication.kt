package com.stiproject.kelassti

import android.app.Application
import androidx.work.Configuration
import com.stiproject.kelassti.data.repository.AuthRepository
import com.stiproject.kelassti.data.repository.DosenRepository
import com.stiproject.kelassti.data.repository.GetUserRepository
import com.stiproject.kelassti.data.repository.KasRepository
import com.stiproject.kelassti.data.repository.MahasiswaRepository
import com.stiproject.kelassti.data.repository.TasksRepository
import com.stiproject.kelassti.presentation.state.UserDataHolder
import com.stiproject.kelassti.worker.KasReminderNotificationWorker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@HiltAndroidApp
class MyApplication: Application(), Configuration.Provider {

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()

    override fun onCreate() {
        super.onCreate()
        KasReminderNotificationWorker.setupDailyCheck(this)
    }

    @Module
    @InstallIn(SingletonComponent::class)
    object AppModule{

        @Provides
        @Singleton
        fun provideUserDataHolder() = UserDataHolder()

        @Provides
        @Singleton
        fun provideAuthRepository() = AuthRepository()

        @Provides
        @Singleton
        fun provideGetUserRepository() = GetUserRepository()

        @Provides
        @Singleton
        fun provideMahasiswaRepository() = MahasiswaRepository()        

        @Provides
        @Singleton
        fun provideKasRepository() = KasRepository()

        @Provides
        @Singleton
        fun provideTasksRepository() = TasksRepository()

        @Provides
        @Singleton
        fun provideDosenRepository() = DosenRepository()
    }
}