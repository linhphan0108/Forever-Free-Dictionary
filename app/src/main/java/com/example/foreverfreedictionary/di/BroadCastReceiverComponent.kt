package com.example.foreverfreedictionary.di

import android.app.Application
import com.example.foreverfreedictionary.di.module.*
import com.example.foreverfreedictionary.ui.broadcastReceiver.OnActionReminderNotificationBroadCastReceiver
import com.example.foreverfreedictionary.ui.broadcastReceiver.OnShowRemindersNotificationBroadCastReceiver
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [BroadCastReceiverModule::class, RetrofitModule::class, DataSourceModule::class,
    MapperModule::class, DataBaseModule::class])
interface BroadCastReceiverComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: Application): Builder
        fun build(): BroadCastReceiverComponent
    }

    fun inject(onShowRemindersNotificationBroadCastReceiver: OnShowRemindersNotificationBroadCastReceiver)
    fun inject(onActionReminderNotificationBroadCastReceiver: OnActionReminderNotificationBroadCastReceiver)
}