package com.example.foreverfreedictionary.ui.mapper

import com.example.foreverfreedictionary.data.cloud.model.Dictionary as DictionaryCloud
import com.example.foreverfreedictionary.data.local.TblDictionary
import com.example.foreverfreedictionary.vo.Resource
import com.example.foreverfreedictionary.vo.Status

class DictionaryMapper {
    /**
     * convert data from domain-layer into data-layer
     */
    fun toDomain(dictionary: DictionaryCloud): TblDictionary {
        return with(dictionary){
            TblDictionary(query, word, dictionary.topic, isCheckSpellPage, content, soundBr, soundAme, ipaBr, ipaAme, false, lastAccess)
        }
    }

    /**
     * convert data from data-layer into domain-layer
     */
    fun fromDomain(resource: Resource<TblDictionary>) : Resource<String>{
        return when(resource.status){
            Status.LOADING -> {Resource.loading()}
            Status.ERROR -> {Resource.error(resource.message)}
            Status.SUCCESS -> {Resource.success(resource.data?.content)}
        }
    }
}