package com.example.foreverfreedictionary.data.local

import com.example.foreverfreedictionary.data.cloud.model.Dictionary
import com.example.foreverfreedictionary.domain.datasource.DictionaryDataDs
import com.example.foreverfreedictionary.vo.Resource

class DictionaryDataLocal :DictionaryDataDs {
    override suspend fun queryDictionaryData(query: String): Resource<Dictionary> {
        return Resource.error("no data")
    }
}