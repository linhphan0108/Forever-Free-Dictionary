package com.example.foreverfreedictionary.di.module

import com.example.foreverfreedictionary.data.cloud.*
import com.example.foreverfreedictionary.data.local.AutoCompletionLocal
import com.example.foreverfreedictionary.data.local.DictionaryDataLocal
import com.example.foreverfreedictionary.data.local.WordOfTheDayLocal
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DataSourceModule {
    @Singleton
    @JvmStatic @Provides
    fun provideAutoCompletionLocal(): AutoCompletionLocal = AutoCompletionLocal()

//    @Singleton
//    @JvmStatic @Provides
//    fun provideAutoCompletionCloud(apiInterface: ApiInterface):
//            AutoCompletionCloud = AutoCompletionCloud(apiInterface)

//    @Singleton
//    @JvmStatic @Provides
//    fun provideDictionaryDataLocal() = DictionaryDataLocal()

    @Singleton
    @JvmStatic @Provides
    fun provideDictionaryDataCloud() = DictionaryDataCloud()

//    @Singleton
//    @JvmStatic @Provides
//    fun provideWordOfTheDayLocal() = WordOfTheDayLocal()

    @Singleton
    @JvmStatic @Provides
    fun provideWordOfTheDayCloud() = WordOfTheDayCloud()

    @Singleton
    @JvmStatic @Provides
    fun provideHistoryCloud() = HistoryCloud()

    @Singleton
    @JvmStatic @Provides
    fun provideFavoriteCloud() = FavoriteCloud()

    @Singleton
    @JvmStatic @Provides
    fun provideReminderCloud() = ReminderCloud()
}