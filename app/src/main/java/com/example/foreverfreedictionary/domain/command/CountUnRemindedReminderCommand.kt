package com.example.foreverfreedictionary.domain.command

import com.example.foreverfreedictionary.domain.provider.ReminderProvider
import com.example.foreverfreedictionary.vo.Resource
import javax.inject.Inject

class CountUnRemindedReminderCommand @Inject constructor(
    private val provider: ReminderProvider
) {

    suspend fun execute(): Resource<Int>{
        val time = System.currentTimeMillis() + 1000*60*60*24
        return provider.countUnRemindedDictionaryReminder(time)
    }
}