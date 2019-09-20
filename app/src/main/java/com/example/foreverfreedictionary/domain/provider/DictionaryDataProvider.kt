package com.example.foreverfreedictionary.domain.provider

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.foreverfreedictionary.data.cloud.DictionaryDataCloud
import com.example.foreverfreedictionary.data.local.room.DictionaryDao
import com.example.foreverfreedictionary.domain.mapper.DictionaryMapper
import com.example.foreverfreedictionary.vo.Resource
import timber.log.Timber
import javax.inject.Inject

class DictionaryDataProvider @Inject constructor(
    private val local: DictionaryDao,
    private val cloud: DictionaryDataCloud,
    private val mapper: DictionaryMapper,
    private val historyProvider: HistoryProvider) {
    suspend fun queryDictionaryData(query: String): LiveData<Resource<String>>{
        return Transformations.map(resultLiveData(
            databaseQuery = {
                local.getDictionary(query)
            },
            cloudCall = {cloud.queryDictionaryData(query)},
            saveCloudData = { cloudData ->
                val rowId = local.insertDictionary(mapper.toData(cloudData))
                Timber.d("insert dictionary into db at $rowId")
                historyProvider.insertHistory(query)
            })){ resource ->
                mapper.toDomain(resource)
            }
    }
}