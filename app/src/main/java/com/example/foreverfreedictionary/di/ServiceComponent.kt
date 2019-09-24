package com.example.foreverfreedictionary.di

import android.app.Application
import com.example.foreverfreedictionary.di.module.*
import com.example.foreverfreedictionary.ui.service.ReminderService
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ServiceModule::class, RetrofitModule::class, DataSourceModule::class,
    MapperModule::class, DataBaseModule::class])
interface ServiceComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: Application): Builder
        fun build(): ServiceComponent
    }

    fun inject(service: ReminderService)
}