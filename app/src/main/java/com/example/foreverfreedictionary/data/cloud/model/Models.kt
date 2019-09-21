package com.example.foreverfreedictionary.data.cloud.model

import com.google.gson.annotations.SerializedName

data class AutoCompletionResponse(@SerializedName("results") val results: List<SearchText>)
data class SearchText(@SerializedName("searchtext")val suggestion: String)
data class Dictionary(
    val word: String,
    val content: String,
    val soundBr: String?,
    val soundAme: String?,
    val ipaBr: String,
    /**american accent*/
    val ipaAme: String? = null,
    val url: String
)