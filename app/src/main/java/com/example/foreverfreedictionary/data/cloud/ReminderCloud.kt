package com.example.foreverfreedictionary.data.cloud

import com.example.foreverfreedictionary.vo.Resource
import java.sql.Date

class ReminderCloud {
    fun getReminders() : Resource<List<String>>{
        return Resource.error("no data")
    }

    fun getUnRemindedReminders() : Resource<List<String>>{
        return Resource.error("no data")
    }

    fun setReminded(queryList: List<String>) : Resource<Int>{
        return Resource.error("no data")
    }

    fun updateReminder(query: String, isReminded: Boolean, time: Date): Resource<Int>{
        return Resource.error("no data")
    }

    fun insertReminder() : Resource<Long> {
        return Resource.success(0L)
    }

    fun deleteReminder() : Resource<Int> {
        return Resource.success(0)
    }
}