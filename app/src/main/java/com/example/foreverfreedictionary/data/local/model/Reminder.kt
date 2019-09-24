package com.example.foreverfreedictionary.data.local.model

import androidx.room.ColumnInfo
import java.sql.Date

data class Reminder(val query: String,
                    val word: String,
                    val soundBr: String?,
                    val soundAme: String?,
                    val ipaBr: String?,
                    val ipaAme: String?,
                    val isReminded: Boolean,
                    @ColumnInfo(name = "time") val remindTime: Date )