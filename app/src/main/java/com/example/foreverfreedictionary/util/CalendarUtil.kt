package com.example.foreverfreedictionary.util

import java.util.*

class CalendarUtil {
    companion object{
        fun howLongTilNext8Clock(): Long{
            val todayAt8OClock = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 8)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val next8OClock = Calendar.getInstance()
            val currentIntMillis = next8OClock.timeInMillis
            if (next8OClock.timeInMillis > todayAt8OClock.timeInMillis){
                next8OClock.add(Calendar.DAY_OF_MONTH, 1)
            }
            next8OClock.set(Calendar.HOUR_OF_DAY, 8)
            next8OClock.set(Calendar.MINUTE, 0)
            next8OClock.set(Calendar.SECOND, 0)
            next8OClock.set(Calendar.MILLISECOND, 0)
            return next8OClock.timeInMillis - currentIntMillis
        }

    }
}