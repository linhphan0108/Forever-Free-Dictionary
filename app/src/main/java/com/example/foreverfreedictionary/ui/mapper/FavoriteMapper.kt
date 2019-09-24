package com.example.foreverfreedictionary.ui.mapper

import com.example.foreverfreedictionary.data.local.TblDictionary
import com.example.foreverfreedictionary.ui.model.FavoriteEntity
import com.example.foreverfreedictionary.vo.Resource
import com.example.foreverfreedictionary.vo.Status

class FavoriteMapper {
    fun fromDomain(resource: Resource<List<TblDictionary>>): Resource<List<FavoriteEntity>>{
        return when(resource.status){
            Status.LOADING -> {Resource.loading()}
            Status.ERROR -> {Resource.error(resource.message)}
            Status.SUCCESS -> {
                val mappedData = resource.data!!.map {tblDictionary ->
                    with(tblDictionary){
                        FavoriteEntity(query, word, soundBr, soundAme, ipaBr, ipaAme, isFavorite, lastAccess)
                    }
                }
            Resource.success(mappedData)
            }
        }
    }
}