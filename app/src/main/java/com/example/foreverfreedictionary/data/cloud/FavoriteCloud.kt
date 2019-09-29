package com.example.foreverfreedictionary.data.cloud

class FavoriteCloud {
    fun delete(query: String): ApiResponse<Int>{
        return ApiResponse.create(-1)
    }

    fun insert(): ApiResponse<Long>{
        return ApiResponse.create(-1)
    }
}