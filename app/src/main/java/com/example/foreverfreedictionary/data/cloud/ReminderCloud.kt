package com.example.foreverfreedictionary.data.cloud

import java.sql.Date

class ReminderCloud {
    fun getReminders() : ApiResponse<List<String>>{
        return ApiResponse.create<List<String>>(null)
    }

    fun getUnRemindedReminders() : ApiResponse<List<String>>{
        return ApiResponse.create<List<String>>(null)
    }

    fun setReminded(queryList: List<String>) : ApiResponse<Int>{
        return ApiResponse.create(-1)
    }

    fun updateReminder(query: String, isReminded: Boolean, time: Date): ApiResponse<Int>{
        return ApiResponse.create(-1)
    }

    fun insertReminder() : ApiResponse<Long> {
        return ApiResponse.create(-0L)
    }

    fun deleteReminder() : ApiResponse<Int> {
        return ApiResponse.create(-1)
    }
}