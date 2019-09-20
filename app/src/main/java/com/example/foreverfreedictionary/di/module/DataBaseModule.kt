package com.example.foreverfreedictionary.di.module

import android.app.Application
import com.example.foreverfreedictionary.data.local.room.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DataBaseModule {
    @Singleton
    @JvmStatic @Provides
    fun provideDb(app: Application) = AppDatabase.getInstance(app)

    @Singleton
    @JvmStatic @Provides
    fun provideDictionaryDao(db: AppDatabase) = db.dictionaryDao()

    @Singleton
    @JvmStatic @Provides
    fun provideWordOfTheDayDao(db: AppDatabase) = db.wordOfTheDayDao()

    @Singleton
    @JvmStatic @Provides
    fun provideHistoryDao(db: AppDatabase) = db.historyDao()
}