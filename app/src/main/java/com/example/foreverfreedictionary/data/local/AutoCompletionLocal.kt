package com.example.foreverfreedictionary.data.local

import com.example.foreverfreedictionary.domain.datasource.AutoCompletionDS
import com.example.foreverfreedictionary.vo.Resource

class AutoCompletionLocal : AutoCompletionDS {
    override suspend fun fetchAutoCompletion(query: String): Resource<List<String>> {
        return Resource.error("no data")
    }
}