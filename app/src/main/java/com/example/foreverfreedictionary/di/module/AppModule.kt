package com.example.foreverfreedictionary.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object AppModule {
    @JvmStatic @Provides
    @Singleton
    fun providesApplicationContext(app: Application): Application = app

    @JvmStatic @Provides
    @Singleton
    fun providesContext(context: Context): Context = context
}