package com.example.foreverfreedictionary.ui.broadcastReceiver

import android.app.Application
import android.content.Context
import android.content.Intent
import com.example.foreverfreedictionary.di.DaggerBroadCastReceiverComponent
import com.example.foreverfreedictionary.domain.command.SetRemindedRemindersCommand
import com.example.foreverfreedictionary.ui.mapper.ReminderMapper
import com.example.foreverfreedictionary.ui.screen.main.MainActivity
import com.example.foreverfreedictionary.util.NotificationUtil
import com.example.foreverfreedictionary.vo.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class OnActionReminderNotificationBroadCastReceiver: BaseBroadCastReceiver() {

    @Inject
    lateinit var setRemindedRemindersCommand: SetRemindedRemindersCommand
    @Inject
    lateinit var reminderMapper: ReminderMapper

    override fun onReceive(context: Context, intent: Intent) {
        DaggerBroadCastReceiverComponent.builder().application(context.applicationContext as Application).build().inject(this)
        val queryList = intent.getStringArrayListExtra(NotificationUtil.ARG_REMINDERS_QUERY_LIST)?.toList()
        if (queryList.isNullOrEmpty()) return
        when(intent.action){
            NotificationUtil.ACTION_REMINDER_SET_REMINDED -> {
                setReminded(queryList)
            }
            NotificationUtil.ACTION_REMINDER_SET_REMINDED_AND_OPEN_REMINDER_SCREEN -> {
                setReminded(queryList)
                val i = Intent(context, MainActivity::class.java)
                i.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(i)
            }
        }
    }

    private fun setReminded(queryList: List<String>) {
        uiScope.launch {
            val deferred = async(Dispatchers.IO){
                setRemindedRemindersCommand.queryList = queryList
                setRemindedRemindersCommand.execute()
            }
            val resource = deferred.await()
            when(resource.status){
                Status.LOADING -> {

                }
                Status.ERROR -> {

                }
                Status.SUCCESS -> {
                    Timber.d("${resource.data} reminders reminded")
                }
            }
        }
    }
}