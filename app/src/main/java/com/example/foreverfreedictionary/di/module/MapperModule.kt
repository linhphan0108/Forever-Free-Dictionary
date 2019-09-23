package com.example.foreverfreedictionary.di.module

import com.example.foreverfreedictionary.domain.mapper.DictionaryMapper
import com.example.foreverfreedictionary.domain.mapper.HistoryMapper
import com.example.foreverfreedictionary.domain.mapper.WordOfTheDayMapper
import com.example.foreverfreedictionary.ui.mapper.AutoCompletionMapper
import com.example.foreverfreedictionary.ui.mapper.FavoriteMapper as FavoriteUiMapper
import com.example.foreverfreedictionary.ui.mapper.HistoryMapper as HistoryUiMapper
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
    fun provideWordOfTheDayMapper() = WordOfTheDayMapper()

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
    fun provideFavoriteUiMapper() = FavoriteUiMapper()
}