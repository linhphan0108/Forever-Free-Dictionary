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
            "$days d"
        }
        hours > 0 -> {
            "$hours h"
        }
        minutes > 0 -> {
            "$minutes min"
        }
        else -> "recently"
    }
}