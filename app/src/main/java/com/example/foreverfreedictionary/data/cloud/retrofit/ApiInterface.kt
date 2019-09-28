package com.example.foreverfreedictionary.data.cloud.retrofit

import androidx.lifecycle.LiveData
import com.example.foreverfreedictionary.data.cloud.ApiResponse
import com.example.foreverfreedictionary.data.cloud.model.AutoCompletionResponse
import retrofit2.http.*


interface ApiInterface {
    @GET("autocomplete/english/")
    @Headers("Accept: application/json", "Content-Type: application/json")
    fun getSearchAutoCompletion(@Query("q") query: String): LiveData<ApiResponse<AutoCompletionResponse>>
}