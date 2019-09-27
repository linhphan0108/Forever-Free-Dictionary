package com.example.foreverfreedictionary.data.local.model

import androidx.room.ColumnInfo
import java.sql.Date

class FavoriteDictionary(val query: String,
                         val word: String,
                         val soundBr: String?,
                         val soundAme: String?,
                         val ipaBr: String?,
                         val ipaAme: String?,
                         val isFavorite: Boolean,
                         val isReminded: Boolean,
                         @ColumnInfo(name = "time")
                         val remindTime: Date?)