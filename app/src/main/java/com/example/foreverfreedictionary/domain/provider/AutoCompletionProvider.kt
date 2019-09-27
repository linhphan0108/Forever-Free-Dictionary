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
    private val autoCompletionCloud: AutoCompletionCloud) : BaseProvider() {

    suspend fun fetchAutoCompletion(query: String): LiveData<Resource<List<String>>> {
        val result: Resource<List<String>?> = firstSource(
            databaseQuery = {
                autoCompletionLocal.fetchAutoCompletion(query)
            }, cloudCall = {
                autoCompletionCloud.fetchAutoCompletion(query)
            }, mapper = {
                it
            }
        )
        val liveData: MutableLiveData<Resource<List<String>>> = MutableLiveData()
            liveData.postValue(if (result.status == Status.SUCCESS && !result.data.isNullOrEmpty()){
                Resource.success(result.data)
            }else{
                autoCompletionCloud.fetchAutoCompletion(query)
            })

        return liveData
    }
}