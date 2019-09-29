package com.example.foreverfreedictionary.ui.service

import android.app.Activity
import android.app.IntentService
import android.content.Intent
import com.example.foreverfreedictionary.di.DaggerServiceComponent
import com.example.foreverfreedictionary.domain.command.CountUnRemindedReminderCommand
import com.example.foreverfreedictionary.ui.mapper.ReminderMapper
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject
import com.example.foreverfreedictionary.ui.broadcastReceiver.OnShowRemindersNotificationBroadCastReceiver


class ReminderService @Inject constructor(
) : IntentService(ReminderService::javaClass.name){

    companion object{
        const val ARG_REMINDERS_IN_TIME = "ARG_REMINDERS_IN_TIME"
    }

    @Inject lateinit var countUnRemindedReminderCommand: CountUnRemindedReminderCommand
    @Inject lateinit var reminderMapper: ReminderMapper
    private val job: Job by lazy { Job() }
    private val intentServiceScope: CoroutineScope by lazy { CoroutineScope(
        Dispatchers.Default + job) }

    override fun onCreate() {
        DaggerServiceComponent.builder().application(application).build().inject(this)

         /**
         * Note: `super` needs to be after `.inject`
         */
        super.onCreate()
    }

    override fun onHandleIntent(intent: Intent?) {
        Timber.d("ReminderService")
        intentServiceScope.launch {
            val resource = countUnRemindedReminderCommand.execute()
//            when(resource.status){
//                Status.LOADING -> {}
//                Status.ERROR -> {}
//                Status.SUCCESS ->{
//                    val count = resource.data ?: 0
//                    if(count > 0){
//                        onHasRemindersInTime()
//                    }
//                }
//            }
        }
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    private fun onHasRemindersInTime() {
        val intent = Intent(this, OnShowRemindersNotificationBroadCastReceiver::class.java)
        intent.putExtra("resultCode", Activity.RESULT_OK)
        sendBroadcast(intent)
    }
}
