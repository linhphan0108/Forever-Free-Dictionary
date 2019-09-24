package com.example.foreverfreedictionary.di.module

import com.example.foreverfreedictionary.ui.service.ReminderService
import dagger.Module
import dagger.Provides



@Module
class ServiceModule {
    @Provides
    fun provideReminderService(mService: ReminderService): ReminderService {
        return mService
    }
}