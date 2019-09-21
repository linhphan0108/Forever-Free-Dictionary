package com.example.foreverfreedictionary.domain.provider

import androidx.lifecycle.LiveData
import com.example.foreverfreedictionary.data.cloud.HistoryCloud
import com.example.foreverfreedictionary.data.local.model.DictionaryHistory
import com.example.foreverfreedictionary.data.local.room.HistoryDao
import com.example.foreverfreedictionary.domain.mapper.HistoryMapper
import com.example.foreverfreedictionary.vo.Resource
import javax.inject.Inject

class HistoryProvider @Inject constructor(
    private val local: HistoryDao,
    private val cloud: HistoryCloud,
    private val historyMapper: HistoryMapper
) {
    suspend fun getHistory(): LiveData<Resource<List<DictionaryHistory>>> {
        return resultLiveData(databaseQuery = {
            local.getDictionaryHistory()
        }, cloudCall = {
            cloud.getHistory()
        }, saveCloudData = {

        })
    }

    fun insertHistory(query: String){
        local.insertHistory(historyMapper.toData(query))
    }
}