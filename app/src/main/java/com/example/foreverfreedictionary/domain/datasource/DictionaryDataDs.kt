package com.example.foreverfreedictionary.domain.datasource

import com.example.foreverfreedictionary.data.cloud.model.Dictionary
import com.example.foreverfreedictionary.vo.Resource

interface DictionaryDataDs {
    suspend fun queryDictionaryData(query: String): Resource<Dictionary>
}