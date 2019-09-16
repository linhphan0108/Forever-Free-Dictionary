package com.example.foreverfreedictionary.data.local

import com.example.foreverfreedictionary.domain.datasource.DictionaryDataDs
import com.example.foreverfreedictionary.vo.Resource

class DictionaryDataLocal :DictionaryDataDs {
    override suspend fun queryDictionaryData(query: String): Resource<String> {
        return Resource.error("no data")
    }
}