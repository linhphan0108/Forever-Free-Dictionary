package com.example.foreverfreedictionary.data.cloud.model

import com.google.gson.annotations.SerializedName
import java.sql.Date

data class AutoCompletionResponse(@SerializedName("results") val results: List<SearchText>)
data class SearchText(@SerializedName("searchtext")val suggestion: String)
data class Dictionary(
    val query: String,
    val word: String,
    val topic: String?,
    val isCheckSpellPage: Boolean = false,
    val content: String,
    val soundBr: String?,
    val soundAme: String?,
    val ipaBr: String?,
    val ipaAme: String? = null,
    val url: String,
    val lastAccess: Date
)