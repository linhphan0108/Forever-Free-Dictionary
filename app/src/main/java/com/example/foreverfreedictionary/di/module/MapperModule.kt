package com.example.foreverfreedictionary.di.module

import com.example.foreverfreedictionary.domain.mapper.*
import com.example.foreverfreedictionary.ui.mapper.DictionaryMapper as DictionaryUiMapper
import com.example.foreverfreedictionary.ui.mapper.WordOfTheDayMapper as WordOfTheDayUiMapper
import com.example.foreverfreedictionary.ui.mapper.ReminderMapper as ReminderUiMapper
import com.example.foreverfreedictionary.ui.mapper.FavoriteMapper as FavoriteUiMapper
import com.example.foreverfreedictionary.ui.mapper.HistoryMapper as HistoryUiMapper
import com.example.foreverfreedictionary.ui.mapper.AutoCompletionMapper as AutoCompletionUiMapper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object MapperModule{
    @Singleton
    @JvmStatic @Provides
    fun provideDictionaryMapper() = DictionaryMapper()

    @Singleton
    @JvmStatic @Provides
    fun provideDictionaryUiMapper() = DictionaryUiMapper()

    @Singleton
    @JvmStatic @Provides
    fun provideWordOfTheDayMapper() = WordOfTheDayMapper()

    @Singleton
    @JvmStatic @Provides
    fun provideWordOfTheDayUiMapper() = WordOfTheDayUiMapper()

    @Singleton
    @JvmStatic @Provides
    fun provideAutoCompletionMapper() = AutoCompletionMapper()

    @Singleton
    @JvmStatic @Provides
    fun provideHistoryMapper() = HistoryMapper()

    @Singleton
    @JvmStatic @Provides
    fun provideHistoryUiMapper() = HistoryUiMapper()

    @Singleton
    @JvmStatic @Provides
    fun provideFavoriteMapper() = FavoriteMapper()

    @Singleton
    @JvmStatic @Provides
    fun provideFavoriteUiMapper() = FavoriteUiMapper()

    @Singleton
    @JvmStatic @Provides
    fun provideReminderUiMapper() = ReminderUiMapper()

    @Singleton
    @JvmStatic @Provides
    fun provideAutoCompletionUiMapper() = AutoCompletionUiMapper()
}