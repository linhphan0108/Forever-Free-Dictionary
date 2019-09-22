package com.example.foreverfreedictionary.data.local.model

import java.sql.Date


data class DictionaryHistory(val query: String, val word: String, val topic: String?, val isCheckSpellPage: Boolean, val ipaBr: String?,
                             val ipaAme: String?, val lastAccess: Date)