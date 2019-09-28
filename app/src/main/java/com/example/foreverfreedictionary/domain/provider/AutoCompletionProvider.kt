package com.example.foreverfreedictionary.domain.provider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.foreverfreedictionary.data.cloud.retrofit.ApiInterface
import com.example.foreverfreedictionary.data.cloud.ApiResponse
import com.example.foreverfreedictionary.data.cloud.model.AutoCompletionResponse
import com.example.foreverfreedictionary.data.local.AutoCompletionLocal
import com.example.foreverfreedictionary.vo.Resource
import javax.inject.Inject

class AutoCompletionProvider @Inject constructor(
    private val autoCompletionLocal: AutoCompletionLocal,
    private val apiInterface: ApiInterface
) : BaseProvider() {

    fun fetchAutoCompletion(query: String): LiveData<Resource<List<String>>> {
        return object: NetworkBoundResource<List<String>, AutoCompletionResponse>(){
            override fun saveCallResult(item: AutoCompletionResponse) {
                autoCompletionLocal.fetchAutoCompletion(query)
            }

            override fun shouldFetch(data: List<String>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<List<String>> {
                return MutableLiveData<List<String>>().apply { this.postValue(listOf()) }
            }

            override fun createCall(): LiveData<ApiResponse<AutoCompletionResponse>> {
                return apiInterface.getSearchAutoCompletion(query)
            }

            override fun hasOfflineMode():Boolean = false

            override fun mapRequestTypeToResultType(requestResult: AutoCompletionResponse?): List<String> {
                val results = requestResult?.results?.map {
                    it.suggestion
                }
                return results ?: listOf()
            }
        }.asLiveData()
    }
}