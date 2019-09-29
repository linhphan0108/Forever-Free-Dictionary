package com.example.foreverfreedictionary.domain.mapper

import com.example.foreverfreedictionary.data.cloud.ApiEmptyResponse
import com.example.foreverfreedictionary.data.cloud.ApiErrorResponse
import com.example.foreverfreedictionary.data.cloud.ApiResponse
import com.example.foreverfreedictionary.data.cloud.ApiSuccessResponse
import com.example.foreverfreedictionary.data.cloud.model.AutoCompletionResponse
import com.example.foreverfreedictionary.vo.Resource

class AutoCompletionMapper {
    fun fromData(apiResponse: ApiResponse<AutoCompletionResponse>): Resource<List<String>> {
        return when(apiResponse){
            is ApiEmptyResponse -> {
                Resource.error("unknown error")
            }
            is ApiErrorResponse -> {
                Resource.error(apiResponse.errorMessage)
            }
            is ApiSuccessResponse -> {
                Resource.success(apiResponse.data.results.map {searchText ->
                    searchText.suggestion
                })
            }
        }
    }
}