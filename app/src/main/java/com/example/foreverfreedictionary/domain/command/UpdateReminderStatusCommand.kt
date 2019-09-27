package com.example.foreverfreedictionary.domain.command

import com.example.foreverfreedictionary.domain.provider.ReminderProvider
import com.example.foreverfreedictionary.vo.Resource
import java.sql.Date
import javax.inject.Inject

class UpdateReminderStatusCommand @Inject constructor(
    private val provider: ReminderProvider
): BaseResourceCommand<Int>(){
    lateinit var query: String
    var isReminded: Boolean = false
    lateinit var time: Date
    override suspend fun execute(): Resource<Int> {
        return provider.updateReminder(query, isReminded, time)
    }
}