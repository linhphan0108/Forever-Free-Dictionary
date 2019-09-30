package com.example.foreverfreedictionary.domain.provider

import androidx.lifecycle.LiveData
import com.example.foreverfreedictionary.data.cloud.AutoCompletionCloud
import com.example.foreverfreedictionary.data.local.AutoCompletionLocal
import com.example.foreverfreedictionary.domain.mapper.AutoCompletionMapper
import com.example.foreverfreedictionary.vo.Resource
import javax.inject.Inject

class AutoCompletionProvider @Inject constructor(
    private val autoCompletionLocal: AutoCompletionLocal,
    private val cloud: AutoCompletionCloud,
    private val autoCompletionMapper: AutoCompletionMapper
) : BaseProvider() {

    suspend fun fetchAutoCompletion(query: String): LiveData<Resource<List<String>?>> {
        return firstSourceLiveData(dbCall = {
            autoCompletionLocal.fetchAutoCompletion(query)
        }, cloudCall = {
            cloud.fetchAutoCompletion(query)
        }, mapper = {
            autoCompletionMapper.fromData(it)
        })
    }
}