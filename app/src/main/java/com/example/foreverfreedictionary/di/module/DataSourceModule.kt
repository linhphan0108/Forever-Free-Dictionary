package com.example.foreverfreedictionary.di.module

import com.example.foreverfreedictionary.data.cloud.ApiInterface
import com.example.foreverfreedictionary.data.cloud.AutoCompletionCloud
import com.example.foreverfreedictionary.data.local.AutoCompletionLocal
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DataSourceModule {
    @Singleton
    @JvmStatic @Provides
    fun provideAutoCompletionLocal(): AutoCompletionLocal = AutoCompletionLocal()

    @Singleton
    @JvmStatic @Provides
    fun provideAutoCompletionCloud(apiInterface: ApiInterface):
            AutoCompletionCloud = AutoCompletionCloud(apiInterface)
}