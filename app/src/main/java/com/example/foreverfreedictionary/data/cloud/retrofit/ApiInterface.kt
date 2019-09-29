package com.example.foreverfreedictionary.data.cloud.retrofit

import com.example.foreverfreedictionary.data.cloud.model.AutoCompletionResponse
import retrofit2.Response
import retrofit2.http.*


interface ApiInterface {
    @GET("autocomplete/english/")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun getSearchAutoCompletion(@Query("q") query: String): Response<AutoCompletionResponse>
}