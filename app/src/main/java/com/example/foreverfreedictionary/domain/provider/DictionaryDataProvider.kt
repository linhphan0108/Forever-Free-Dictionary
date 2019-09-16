package com.example.foreverfreedictionary.domain.provider

import com.example.foreverfreedictionary.data.cloud.DictionaryDataCloud
import com.example.foreverfreedictionary.data.local.DictionaryDataLocal
import com.example.foreverfreedictionary.vo.Resource
import com.example.foreverfreedictionary.vo.Status
import javax.inject.Inject

class DictionaryDataProvider @Inject constructor(
    private val dictionaryDataLocal: DictionaryDataLocal,
    private val dictionaryDataCloud: DictionaryDataCloud
) {
    suspend fun queryDictionaryData(query: String): Resource<String>{
        val result = dictionaryDataLocal.queryDictionaryData(query)
        return if (result.status == Status.SUCCESS && !result.data.isNullOrBlank()){
            result
        }else{
            dictionaryDataCloud.queryDictionaryData(query)
        }
    }
}