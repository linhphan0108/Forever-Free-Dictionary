package com.example.foreverfreedictionary.ui.model

import androidx.room.ColumnInfo
import java.sql.Date

class DictionaryEntity(
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
): Entity()