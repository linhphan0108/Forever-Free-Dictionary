package com.example.foreverfreedictionary.domain.command

import androidx.lifecycle.LiveData
import com.example.foreverfreedictionary.data.local.TblReminder
import com.example.foreverfreedictionary.domain.provider.ReminderProvider
import com.example.foreverfreedictionary.vo.Resource
import javax.inject.Inject

class InsertReminderCommand @Inject constructor(
    private val provider: ReminderProvider
): BaseCommand<Long>() {
    lateinit var reminder: TblReminder
    override suspend fun execute(): LiveData<Resource<Long>> {
        return provider.insertReminder(reminder)
    }
}