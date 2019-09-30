package com.example.foreverfreedictionary.ui.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.foreverfreedictionary.ui.service.CheckingReminderService
import timber.log.Timber

class OnAlarmReminderBroadCastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Timber.d("OnAlarmReminderBroadCastReceiver")
        val serviceIntent = Intent(context, CheckingReminderService::class.java)
        context.startService(serviceIntent)
    }
}