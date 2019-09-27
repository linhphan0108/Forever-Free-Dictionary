package com.example.foreverfreedictionary.domain.command

import com.example.foreverfreedictionary.domain.provider.ReminderProvider
import com.example.foreverfreedictionary.vo.Resource
import javax.inject.Inject

class DeleteReminderCommand @Inject constructor(
    private val reminderProvider: ReminderProvider
) : BaseResourceCommand<Int>() {
    lateinit var query: String
    override suspend fun execute(): Resource<Int> {
        return reminderProvider.deleteReminder(query)
    }
}