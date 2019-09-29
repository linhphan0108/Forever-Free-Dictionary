package com.example.foreverfreedictionary.domain.command

import androidx.lifecycle.LiveData
import com.example.foreverfreedictionary.domain.provider.ReminderProvider
import com.example.foreverfreedictionary.vo.Resource
import javax.inject.Inject

const val ONE_HOUR = 1000*60*60L
class CountUnRemindedReminderCommand @Inject constructor(
    private val provider: ReminderProvider
) {

    suspend fun execute(): LiveData<Resource<Int>> {
        val time = System.currentTimeMillis() + ONE_HOUR
        return provider.countUnRemindedDictionaryReminder(time)
    }
}