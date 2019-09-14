package com.example.foreverfreedictionary.ui

import android.app.Application
import com.example.foreverfreedictionary.di.ApplicationComponent
import com.example.foreverfreedictionary.di.DaggerApplicationComponent
import com.example.foreverfreedictionary.di.DaggerComponentProvider

class ForeverFreeDictionaryApplication : Application(), DaggerComponentProvider {
    override val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.builder()
            .application(this)
            .build()
    }
}