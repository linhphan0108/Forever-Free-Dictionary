package com.example.foreverfreedictionary.extensions

import java.sql.Date
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
            if (days == 1L) "$days day" else "$days days"
        }
        hours > 0 -> {
            if (hours == 1L) "$hours hour" else "$hours hours"
        }
        minutes > 0 -> {
            if (minutes == 1L) "$minutes minute" else "$minutes minutes"
        }
        else -> "recently"
    }
}