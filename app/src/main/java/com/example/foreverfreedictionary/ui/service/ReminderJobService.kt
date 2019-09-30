package com.example.foreverfreedictionary.ui.service

import android.annotation.TargetApi
import android.app.Activity
import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Build
import android.content.Intent
import com.example.foreverfreedictionary.di.DaggerServiceComponent
import com.example.foreverfreedictionary.domain.command.CountUnRemindedReminderCommand
import com.example.foreverfreedictionary.ui.broadcastReceiver.OnShowRemindersNotificationBroadCastReceiver
import com.example.foreverfreedictionary.ui.mapper.ReminderMapper
import com.example.foreverfreedictionary.util.ReminderUtil
import com.example.foreverfreedictionary.vo.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class ReminderJobService: JobService() {
    @Inject
    lateinit var countUnRemindedReminderCommand: CountUnRemindedReminderCommand
    @Inject
    lateinit var reminderMapper: ReminderMapper
    private val job: Job by lazy { Job() }
    private val intentServiceScope: CoroutineScope by lazy { CoroutineScope(
        Dispatchers.Default + job) }

    override fun onCreate() {
        DaggerServiceComponent.builder().application(application).build().inject(this)
        super.onCreate()
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Timber.d("onStartJob")
        intentServiceScope.launch {
            val resource = countUnRemindedReminderCommand.execute()
            when(resource.status){
                Status.LOADING -> {}
                Status.ERROR -> {}
                Status.SUCCESS ->{
                    val count = resource.data ?: 0
                    Timber.d("has $count in reminders")
                    if(count > 0){
                        onHasRemindersInTime()
                        jobFinished(params, false)
                    }
                }
            }
        }

        ReminderUtil.scheduleReminder(applicationContext) // reschedule the job
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Timber.d("onStopJob")
        job.cancel()
        return true
    }



    private fun onHasRemindersInTime() {
        val intent = Intent(this, OnShowRemindersNotificationBroadCastReceiver::class.java)
        intent.putExtra("resultCode", Activity.RESULT_OK)
        sendBroadcast(intent)
    }
}