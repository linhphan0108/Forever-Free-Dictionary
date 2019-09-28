package com.example.foreverfreedictionary.di.module

import com.example.foreverfreedictionary.data.cloud.retrofit.ApiInterface
import com.example.foreverfreedictionary.data.cloud.retrofit.ServiceGenerator
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object RetrofitModule {
    @JvmStatic @Provides
    @Singleton
    fun provideRetrofit(): ApiInterface = ServiceGenerator.createService(
        ApiInterface::class.java)
}