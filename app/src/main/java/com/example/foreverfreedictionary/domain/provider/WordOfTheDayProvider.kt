package com.example.foreverfreedictionary.domain.provider

import com.example.foreverfreedictionary.data.cloud.WordOfTheDayCloud
import com.example.foreverfreedictionary.data.local.WordOfTheDayLocal
import com.example.foreverfreedictionary.vo.Resource
import com.example.foreverfreedictionary.vo.Status
import javax.inject.Inject

class WordOfTheDayProvider @Inject constructor(
    private val wordOfTheDayLocal: WordOfTheDayLocal,
    private val  wordOfTheDayCloud: WordOfTheDayCloud) {

    suspend fun fetchWordOfTheDay(): Resource<String> {
        val result = wordOfTheDayLocal.fetchWordOfTheDay()
        return if (result.status == Status.SUCCESS && !result.data.isNullOrBlank()) {
            result
        } else {
            wordOfTheDayCloud.fetchWordOfTheDay()
        }
    }
}