package com.example.foreverfreedictionary.domain.provider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.foreverfreedictionary.data.cloud.DictionaryDataCloud
import com.example.foreverfreedictionary.data.local.TblDictionary
import com.example.foreverfreedictionary.data.local.room.DictionaryDao
import com.example.foreverfreedictionary.domain.mapper.DictionaryMapper
import com.example.foreverfreedictionary.vo.Resource
import timber.log.Timber
import javax.inject.Inject

class DictionaryDataProvider @Inject constructor(
    private val local: DictionaryDao,
    private val cloud: DictionaryDataCloud,
    private val mapper: DictionaryMapper,
    private val historyProvider: HistoryProvider): BaseProvider() {
    suspend fun queryDictionaryData(query: String): LiveData<Resource<String>>{
        return Transformations.map(singleTruthSourceLiveData(
            databaseQuery = {
                local.getDictionary(query)
            },
            cloudCall = {cloud.queryDictionaryData(query)},
            saveCloudData = { cloudData ->
                val rowId = local.insertDictionary(mapper.toData(cloudData))
                Timber.d("insert dictionary into db at $rowId")
                historyProvider.insertHistory(cloudData)
            })){ resource ->
                mapper.fromData(resource)
            }
    }

    fun updateFavorite(query: String, isFavorite: Boolean): LiveData<Resource<Boolean>>{
        val id = local.insertFavoriteDictionary(query, isFavorite)
        val resource = if (id > 0){
            Resource.success(true)
        }else{
            Resource.error("Oops something went wrong.")
        }
        return MutableLiveData<Resource<Boolean>>().apply {
            postValue(resource)
        }
    }

    fun getFavorite(): LiveData<Resource<List<TblDictionary>>>{
        return Transformations.map(local.getFavoriteDictionary()){
            Resource.success(it)
        }
    }
}