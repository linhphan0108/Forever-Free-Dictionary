package com.example.foreverfreedictionary.di.module

import com.example.foreverfreedictionary.ui.broadcastReceiver.OnActionReminderNotificationBroadCastReceiver
import com.example.foreverfreedictionary.ui.broadcastReceiver.OnShowRemindersNotificationBroadCastReceiver
import dagger.Module
import dagger.Provides

@Module
class BroadCastReceiverModule {

    @Provides
    fun provideResponseReminderBroadCastReceiver(responseReminderNotificationBroadCastReceiver: OnShowRemindersNotificationBroadCastReceiver) = responseReminderNotificationBroadCastReceiver

    @Provides
    fun provideOnDeleteReminderBroadCastReceiver(onDeleteReminderBroadCastReceiver: OnActionReminderNotificationBroadCastReceiver) = onDeleteReminderBroadCastReceiver
}