package com.example.foreverfreedictionary.di

import android.app.Application
import com.example.foreverfreedictionary.di.module.DataSourceModule
import com.example.foreverfreedictionary.di.module.MapperModule
import com.example.foreverfreedictionary.di.module.RetrofitModule
import com.example.foreverfreedictionary.di.module.DataBaseModule
import com.example.foreverfreedictionary.ui.dialog.VoiceRecognizerDialogViewModel
import com.example.foreverfreedictionary.ui.screen.history.HistoryViewModel
import com.example.foreverfreedictionary.ui.screen.home.HomeViewModel
import com.example.foreverfreedictionary.ui.screen.main.MainActivityViewModel
import com.example.foreverfreedictionary.ui.screen.result.ResultActivityViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 * I like to consider that as the Dagger configuration entry point. It unfortunately doesn't look
 * simple (even though it doesn't look that bad either) and it doesn't get much better than that.
 */
@Singleton
@Component(modules = [RetrofitModule::class, DataSourceModule::class,
                    MapperModule::class, DataBaseModule::class])
interface ApplicationComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): ApplicationComponent
    }

    /**
     * We could've chosen to create an inject() method instead and do field injection in the
     * Activity, but for this case this seems less verbose to me in the end.
     */
    val mainActivityViewModel: MainActivityViewModel
    val resultActivityViewModel: ResultActivityViewModel
    val homeViewModel: HomeViewModel
    val historyViewModel: HistoryViewModel
    val voiceRecognizerDialogViewModel: VoiceRecognizerDialogViewModel
}