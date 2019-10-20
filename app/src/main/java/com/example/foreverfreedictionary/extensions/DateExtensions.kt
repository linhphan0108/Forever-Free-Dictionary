package com.example.foreverfreedictionary.extensions

import java.sql.Date
import java.util.*

const val SECOND: Long = 1000
const val MINUTE = 60*1000
const val HOUR = 60*60*1000
const val DAY = 24*60*60*1000

fun Date.howLongTimeLapsedTilNow(): String{
    val currentMillis = System.currentTimeMillis()
    var timeLapsedMillis = currentMillis - time
    val days = timeLapsedMillis / DAY
    timeLapsedMillis %= DAY
    val hours = timeLapsedMillis / HOUR
    timeLapsedMillis %= HOUR
    val minutes = timeLapsedMillis / MINUTE

    return when {
        days > 0 -> {
            "$days d"
        }
        hours > 0 -> {
            "$hours h"
        }
        minutes > 0 -> {
            "$minutes m"
        }
        else -> "recent"
    }
}


fun Date.howLongTilNext8Clock(): Long{
    val todayAt8OClock = Calendar.getInstance().apply {
        time = this@howLongTilNext8Clock
        set(Calendar.HOUR_OF_DAY, 8)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    val calendar = Calendar.getInstance()
    calendar.time = this
    if (calendar.timeInMillis > todayAt8OClock.timeInMillis){
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }
    calendar.set(Calendar.HOUR_OF_DAY, 8)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis - this.time
}

fun Date.getTomorrow0Clock(): Date{
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.DAY_OF_MONTH, 1)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return Date(calendar.timeInMillis)
}