package com.example.foreverfreedictionary.data.local.model

import java.sql.Date


data class DictionaryHistory(val query: String, val word: String, val isTopic: String?, val ipaBr: String?,
                             val ipaAme: String?, val lastAccess: Date)