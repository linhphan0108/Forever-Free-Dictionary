package com.example.foreverfreedictionary.di.module

import com.example.foreverfreedictionary.ui.service.CheckingReminderService
import dagger.Module
import dagger.Provides



@Module
class ServiceModule {
    @Provides
    fun provideReminderService(mService: CheckingReminderService): CheckingReminderService {
        return mService
    }
}