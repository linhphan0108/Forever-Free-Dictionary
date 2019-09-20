package com.example.foreverfreedictionary.data.cloud

import com.example.foreverfreedictionary.vo.Resource

class HistoryCloud {
    fun getHistory(): Resource<String>{
        return Resource.error("no data")
    }
}