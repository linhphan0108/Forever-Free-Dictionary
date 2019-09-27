package com.example.foreverfreedictionary.domain.command

import com.example.foreverfreedictionary.domain.provider.ReminderProvider
import com.example.foreverfreedictionary.vo.Resource
import javax.inject.Inject

class SetRemindedRemindersCommand@Inject constructor(
    private val provider: ReminderProvider
): BaseResourceCommand<Int>(){
    lateinit var queryList: List<String>
    override suspend fun execute(): Resource<Int> {
        return provider.setReminded(queryList)
    }
}