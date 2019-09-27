package com.example.foreverfreedictionary.ui.mapper

import com.example.foreverfreedictionary.data.local.model.DictionaryHistory
import com.example.foreverfreedictionary.ui.model.HistoryEntity
import com.example.foreverfreedictionary.vo.Resource
import com.example.foreverfreedictionary.vo.Status

class HistoryMapper {
    fun fromDomain(resource: Resource<List<DictionaryHistory>>) : Resource<List<HistoryEntity>>{
        return when (resource.status) {
            Status.LOADING -> {
                Resource.loading()
            }
            Status.SUCCESS -> {
                val mappedData = resource.data!!.map {
                    return@map with(it){
                        HistoryEntity(query, word, soundBr, soundAme, ipaBr, ipaAme,
                            isFavorite, isReminded, remindTime, lastAccess)
                    }
                }
                Resource.success(mappedData)
            }
            Status.ERROR -> {
                Resource.error(resource.message)
            }
        }
    }
}