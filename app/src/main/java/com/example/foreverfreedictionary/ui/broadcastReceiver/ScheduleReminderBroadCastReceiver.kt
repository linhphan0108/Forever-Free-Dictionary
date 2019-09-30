package com.example.foreverfreedictionary.ui.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.foreverfreedictionary.util.ReminderUtil
import timber.log.Timber


class ScheduleReminderBroadCastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Timber.d("onReceive")
        ReminderUtil.scheduleReminder(context)
    }
}