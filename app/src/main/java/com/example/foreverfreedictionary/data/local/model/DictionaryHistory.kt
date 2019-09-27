package com.example.foreverfreedictionary.data.local.model

import java.sql.Date


data class DictionaryHistory(val query: String,
                             val word: String,
                             val topic: String?,
                             val isCheckSpellPage: Boolean,
                             val soundBr: String,
                             val soundAme: String,
                             val ipaBr: String?,
                             val ipaAme: String?,
                             val isFavorite: Boolean,
                             val lastAccess: Date)