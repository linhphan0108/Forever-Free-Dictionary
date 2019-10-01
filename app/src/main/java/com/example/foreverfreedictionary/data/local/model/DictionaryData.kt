package com.example.foreverfreedictionary.data.local.model

import androidx.room.ColumnInfo
import java.sql.Date

data class DictionaryData(
                      val query: String,
                      val word: String,
                      val topic: String?,
                      val isCheckSpellPage: Boolean,
                      val content: String,
                      val soundBr: String?,
                      val soundAme: String?,
                      val ipaBr: String?,
                      val ipaAme: String?,
                      val isFavorite: Boolean,
                      val isReminded: Boolean,
                      @ColumnInfo(name = "time")
                      val remindTime: Date?,
                      val lastAccess: Date
                      )