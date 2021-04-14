package com.softvision.codingexercise.app.dagger

import android.app.Application
import androidx.room.Room
import com.softvision.codingexercise.repository.db.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

const val DATABASE_NAME = "CodingExercise.db"

@Module
class CacheModule {

    @Singleton
    @Provides
    fun provideAppDatabase(application: Application): AppDatabase =
        Room.databaseBuilder(
            application.applicationContext,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
}
