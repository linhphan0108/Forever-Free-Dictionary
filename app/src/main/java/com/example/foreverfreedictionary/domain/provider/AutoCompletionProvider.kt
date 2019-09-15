package com.example.foreverfreedictionary.domain.provider

import com.example.foreverfreedictionary.data.cloud.AutoCompletionCloud
import com.example.foreverfreedictionary.data.local.AutoCompletionLocal
import com.example.foreverfreedictionary.vo.Resource
import com.example.foreverfreedictionary.vo.Status
import javax.inject.Inject

class AutoCompletionProvider @Inject constructor(
    private val autoCompletionLocal: AutoCompletionLocal,
    private val autoCompletionCloud: AutoCompletionCloud) {

    suspend fun fetchAutoCompletion(query: String): Resource<List<String>> {
        val result = autoCompletionLocal.fetchAutoCompletion(query)
        return if (result.status == Status.SUCCESS && !result.data.isNullOrEmpty()){
            result
        }else{
            autoCompletionCloud.fetchAutoCompletion(query)
        }
    }
}