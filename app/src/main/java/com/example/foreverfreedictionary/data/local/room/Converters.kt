package com.example.foreverfreedictionary.data.local.room

import androidx.room.TypeConverter
import java.sql.Date
import java.util.*


/**
 * Type converters to allow Room to reference complex data types.
 */
class Converters {
    @TypeConverter fun fromCalendar(calendar: Calendar): Long = calendar.timeInMillis

    @TypeConverter fun toCalendar(value: Long): Calendar =
            Calendar.getInstance().apply {
                timeInMillis = value
                Calendar.getInstance()
            }

    @TypeConverter
    fun toDate(dateLong: Long?): Date? {
        return if (dateLong == null) null else Date(dateLong)
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
}