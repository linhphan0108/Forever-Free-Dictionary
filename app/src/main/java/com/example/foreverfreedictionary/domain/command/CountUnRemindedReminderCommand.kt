package com.example.foreverfreedictionary.domain.command

import com.example.foreverfreedictionary.domain.provider.ReminderProvider
import com.example.foreverfreedictionary.vo.Resource
import javax.inject.Inject

const val ONE_DAY = 1000L*60*60*24
class CountUnRemindedReminderCommand @Inject constructor(
    private val provider: ReminderProvider
) {

    suspend fun execute(): Resource<Int>{
        val time = System.currentTimeMillis() + ONE_DAY
        return provider.countUnRemindedDictionaryReminder(time)
    }
}