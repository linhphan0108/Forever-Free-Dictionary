package com.example.foreverfreedictionary.data.cloud

import com.example.foreverfreedictionary.data.cloud.model.AutoCompletionResponse
import com.example.foreverfreedictionary.data.cloud.retrofit.ApiInterface
import java.lang.Exception
import javax.inject.Inject

class AutoCompletionCloud @Inject constructor(private val apiInterface: ApiInterface) {
    suspend fun fetchAutoCompletion(query: String): ApiResponse<AutoCompletionResponse> {
        return try {
            ApiResponse.create(apiInterface.getSearchAutoCompletion(query))
        }catch (e: Exception){
            ApiResponse.create(e)
        }
    }
}