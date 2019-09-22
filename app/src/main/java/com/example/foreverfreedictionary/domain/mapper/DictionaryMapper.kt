package com.example.foreverfreedictionary.domain.mapper

import com.example.foreverfreedictionary.data.cloud.model.Dictionary as DictionaryCloud
import com.example.foreverfreedictionary.data.local.TblDictionary
import com.example.foreverfreedictionary.vo.Resource
import com.example.foreverfreedictionary.vo.Status
import java.sql.Date

class DictionaryMapper {
    /**
     * convert data from domain-layer into data-layer
     */
    fun toData(dictionary: DictionaryCloud): TblDictionary {
        return with(dictionary){
            TblDictionary(query, word, dictionary.topic, isCheckSpellPage, content, soundBr, soundAme, ipaBr, ipaAme, Date(System.currentTimeMillis()))
        }
    }

    /**
     * convert data from data-layer into domain-layer
     */
    fun fromData(resource: Resource<TblDictionary>) : Resource<String>{
        return when(resource.status){
            Status.LOADING -> {Resource.loading()}
            Status.ERROR -> {Resource.error(resource.message)}
            Status.SUCCESS -> {Resource.success(resource.data?.content)}
        }
    }

    /**
     * convert data from data-layer into domain-layer
     */
    fun fromData(resource: TblDictionary) : Resource<String>{
        return Resource.success(resource.content)
    }
}