package com.example.foreverfreedictionary.domain.datasource

import com.example.foreverfreedictionary.vo.Resource

interface WordOfTheDayDs {
    fun fetchWordOfTheDay(): Resource<String>
}