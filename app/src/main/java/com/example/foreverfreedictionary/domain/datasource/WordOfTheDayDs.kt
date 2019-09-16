package com.example.foreverfreedictionary.domain.datasource

import com.example.foreverfreedictionary.vo.Resource

interface WordOfTheDayDs {
    suspend fun fetchWordOfTheDay(): Resource<String>
}