package com.example.foreverfreedictionary.domain.command

import androidx.lifecycle.LiveData
import com.example.foreverfreedictionary.data.local.model.Reminder
import com.example.foreverfreedictionary.domain.provider.ReminderProvider
import com.example.foreverfreedictionary.vo.Resource
import javax.inject.Inject

class FetchRemindersCommand @Inject constructor(
    private val provider: ReminderProvider
) : BaseCommand<List<Reminder>>() {
    override suspend fun execute(): LiveData<Resource<List<Reminder>>> {
        return provider.getReminders()
    }
}