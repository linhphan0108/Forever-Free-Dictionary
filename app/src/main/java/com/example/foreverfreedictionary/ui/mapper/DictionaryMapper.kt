package com.example.foreverfreedictionary.ui.mapper

import com.example.foreverfreedictionary.data.local.model.DictionaryData
import com.example.foreverfreedictionary.ui.model.DictionaryEntity
import com.example.foreverfreedictionary.vo.Resource
import com.example.foreverfreedictionary.vo.Status

class DictionaryMapper {
    /**
     * convert data from data-layer into domain-layer
     */
    fun fromDomain(resource: Resource<DictionaryData>) : Resource<DictionaryEntity>{
        return when(resource.status){
            Status.LOADING -> {Resource.loading()}
            Status.ERROR -> {Resource.error(resource.message)}
            Status.SUCCESS -> {
                Resource.success(if(resource.data != null){
                   with(resource.data){
                       DictionaryEntity(query, word, topic, isCheckSpellPage, content, soundBr, soundAme,
                           ipaBr, ipaAme, isFavorite, isReminded, remindTime, lastAccess)
                   }
                }else{
                    null
                })
            }
        }
    }
}