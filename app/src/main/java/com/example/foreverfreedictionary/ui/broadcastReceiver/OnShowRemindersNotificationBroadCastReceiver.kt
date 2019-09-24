package com.example.foreverfreedictionary.ui.broadcastReceiver

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import com.example.foreverfreedictionary.R
import com.example.foreverfreedictionary.di.DaggerBroadCastReceiverComponent
import com.example.foreverfreedictionary.domain.command.FetchRemindersInTimeCommand
import com.example.foreverfreedictionary.ui.mapper.ReminderMapper
import com.example.foreverfreedictionary.ui.model.ReminderEntity
import com.example.foreverfreedictionary.util.NotificationUtil
import com.example.foreverfreedictionary.vo.Resource
import com.example.foreverfreedictionary.vo.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.StringBuilder
import javax.inject.Inject

/**
 * this class will try to fetch all eligible reminders from database, and show a notification
 * to remind users learn vocabulary. these words will be marked as reminded whenever user interact with the notification
 * in both clear or tap on it. if tap the main-screen will be opened.
 */
class OnShowRemindersNotificationBroadCastReceiver : BaseBroadCastReceiver() {
    @Inject
    lateinit var fetchUnRemindedReminderInTimeCommand: FetchRemindersInTimeCommand
    @Inject
    lateinit var reminderMapper: ReminderMapper

    override fun onReceive(context: Context, intent: Intent) {
        DaggerBroadCastReceiverComponent.builder().application(context.applicationContext as Application).build().inject(this)
        Timber.d("OnShowRemindersNotificationBroadCastReceiver")
        val resultCode = intent.getIntExtra("resultCode", RESULT_CANCELED)
        if (resultCode == RESULT_OK) {
            fetchReminders(context)
        }
    }

    private fun fetchReminders(context: Context){
        uiScope.launch {
            val deferred = async(Dispatchers.IO){
                Transformations.map(fetchUnRemindedReminderInTimeCommand.execute()){
                    reminderMapper.fromDomain(it)
                }
            }

            val liveData = deferred.await()
            liveData.observeForever(object :Observer<Resource<List<ReminderEntity>>> {
                override fun onChanged(resource: Resource<List<ReminderEntity>>) {
                    when(resource.status){
                        Status.LOADING -> {}
                        Status.ERROR -> {
//                            liveData.removeObserver(this)
                        }
                        Status.SUCCESS -> {
                            resource.data?.let {reminders ->
                                liveData.removeObserver(this)
                                val words = StringBuilder()
                                val listQuery = mutableListOf<String>()
                                reminders.forEachIndexed { index, reminder ->
                                    words.append("\n${reminder.word} ${reminder.combineIpa(context)}")
                                    listQuery.add(reminder.query)
                                }
                                showReminderNotification(context, words, listQuery)
                            }
                        }
                    }
                }

            })
        }
    }

    private fun showReminderNotification(context: Context, words: StringBuilder, listQuery: MutableList<String>) {
        NotificationUtil(context).let {
            val title = context.getString(R.string.menu_reminder)
            val content = context.getString(if (listQuery.size > 1){
                R.string.reminder_notification_message
            }else{
                R.string.reminder_notification_message_plural
            }, words)
            val builder = it.createReminderNotification(title, content, listQuery)
            it.mManager.notify(NotificationUtil.REMINDER_NOTIFICATION_ID, builder.build())
        }
    }
}