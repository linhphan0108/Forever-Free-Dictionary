package com.example.foreverfreedictionary.domain.provider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.foreverfreedictionary.data.cloud.AutoCompletionCloud
import com.example.foreverfreedictionary.data.local.AutoCompletionLocal
import com.example.foreverfreedictionary.vo.Resource
import com.example.foreverfreedictionary.vo.Status
import javax.inject.Inject

class AutoCompletionProvider @Inject constructor(
    private val autoCompletionLocal: AutoCompletionLocal,
    private val autoCompletionCloud: AutoCompletionCloud) {

    suspend fun fetchAutoCompletion(query: String): LiveData<Resource<List<String>>> {
        val result = autoCompletionLocal.fetchAutoCompletion(query)
        val liveData: MutableLiveData<Resource<List<String>>> = MutableLiveData()
            liveData.postValue(if (result.status == Status.SUCCESS && !result.data.isNullOrEmpty()){
                result
            }else{
                autoCompletionCloud.fetchAutoCompletion(query)
            })

        return liveData
    }
}