package com.example.foreverfreedictionary.domain.datasource

import com.example.foreverfreedictionary.vo.Resource

interface AutoCompletionDS {
    suspend fun fetchAutoCompletion(query: String): Resource<List<String>>
}