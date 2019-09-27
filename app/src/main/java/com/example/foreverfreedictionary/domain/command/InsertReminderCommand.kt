package com.example.foreverfreedictionary.domain.command

import com.example.foreverfreedictionary.data.local.TblReminder
import com.example.foreverfreedictionary.domain.provider.ReminderProvider
import com.example.foreverfreedictionary.vo.Resource
import javax.inject.Inject

class InsertReminderCommand @Inject constructor(
    private val provider: ReminderProvider
): BaseResourceCommand<Long>() {
    lateinit var reminder: TblReminder
    override suspend fun execute(): Resource<Long> {
        return provider.insertReminder(reminder)
    }
}