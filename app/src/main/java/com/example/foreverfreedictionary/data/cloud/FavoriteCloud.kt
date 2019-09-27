package com.example.foreverfreedictionary.data.cloud

import com.example.foreverfreedictionary.vo.Resource

class FavoriteCloud {
    fun delete(query: String): Resource<Int>{
        return Resource.success(0)
    }

    fun insert(): Resource<Long>{
        return Resource.success(0)
    }
}