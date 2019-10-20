package com.example.foreverfreedictionary.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.job.JobScheduler
import android.app.job.JobInfo
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.foreverfreedictionary.extensions.howLongTilNext8Clock
import com.example.foreverfreedictionary.ui.broadcastReceiver.OnAlarmReminderBroadCastReceiver
import com.example.foreverfreedictionary.ui.service.ReminderJobService
import timber.log.Timber
import java.sql.Date

const val REMINDER_JOB_ID = 102
object ReminderUtil {
    fun scheduleReminder(context: Context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            scheduleJob(context)
        }else{
            scheduleAlarm(context)
        }
    }

    // schedule the start of the service every 10 - 30 seconds
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun scheduleJob(context: Context) {
        val serviceComponent = ComponentName(context, ReminderJobService::class.java)
        val builder = JobInfo.Builder(REMINDER_JOB_ID, serviceComponent)
        val timeToStart = CalendarUtil.howLongTilNext8Clock()
        Timber.d("job is set in $timeToStart")
        builder.setMinimumLatency(timeToStart) // wait at least
        builder.setOverrideDeadline(timeToStart + 60000) // maximum delay
        //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
//        builder.setRequiresDeviceIdle(true) // device should be idle
        //builder.setRequiresCharging(false); // we don't care if the device is charging or not
        builder.setPersisted(true)
        val jobScheduler = ContextCompat.getSystemService(context, JobScheduler::class.java)
        jobScheduler?.schedule(builder.build())
    }

    private fun scheduleAlarm(context: Context) {
        val applicationContext = context.applicationContext
        val intent = Intent(applicationContext, OnAlarmReminderBroadCastReceiver::class.java)
        val alarmIntent = PendingIntent.getBroadcast(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val startTime = CalendarUtil.howLongTilNext8Clock()
        val backupAlarmMgr = context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        backupAlarmMgr.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            startTime,
            AlarmManager.INTERVAL_DAY,
            alarmIntent
        )//alarm will repeat after every day
    }
}