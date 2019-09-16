package com.example.foreverfreedictionary.data.local

import com.example.foreverfreedictionary.domain.datasource.WordOfTheDayDs
import com.example.foreverfreedictionary.vo.Resource

class WordOfTheDayLocal : WordOfTheDayDs {
    override suspend fun fetchWordOfTheDay(): Resource<String> {
        return Resource.error("no data")
    }
}