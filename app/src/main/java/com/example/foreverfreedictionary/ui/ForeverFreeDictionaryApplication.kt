package com.example.foreverfreedictionary.ui

import android.app.Application
import com.example.foreverfreedictionary.BuildConfig
import com.example.foreverfreedictionary.di.ApplicationComponent
import com.example.foreverfreedictionary.di.DaggerApplicationComponent
import com.example.foreverfreedictionary.di.DaggerComponentProvider
import com.example.foreverfreedictionary.util.NoLoggingTree
import timber.log.Timber

class ForeverFreeDictionaryApplication : Application(), DaggerComponentProvider {
    override val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.builder()
            .application(this)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        setupTimberLogger()
    }

    private fun setupTimberLogger(){
        if (BuildConfig.DEBUG){
            Timber.plant(object: Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String? {
                    return String.format("C:%s:%s",
                        super.createStackElementTag(element),
                        element.lineNumber)
                }
            })
        }else{
            Timber.plant(NoLoggingTree())
        }
    }
}