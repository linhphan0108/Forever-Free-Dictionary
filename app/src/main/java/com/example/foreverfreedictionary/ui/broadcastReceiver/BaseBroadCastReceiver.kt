package com.example.foreverfreedictionary.ui.broadcastReceiver

import android.content.BroadcastReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

abstract class BaseBroadCastReceiver: BroadcastReceiver() {
    protected val job: Job by lazy { Job() }
    protected val uiScope: CoroutineScope by lazy { CoroutineScope(
        Dispatchers.Main + job) }
}