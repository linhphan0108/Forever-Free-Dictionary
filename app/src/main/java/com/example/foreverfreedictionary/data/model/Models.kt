package com.example.foreverfreedictionary.data.model

import com.google.gson.annotations.SerializedName

data class AutoCompletionResponse(@SerializedName("results") val results: List<SearchText>)
data class SearchText(@SerializedName("searchtext")val suggestion: String)