package com.example.foreverfreedictionary.domain.provider

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.foreverfreedictionary.data.cloud.FavoriteCloud
import com.example.foreverfreedictionary.data.local.model.FavoriteDictionary
import com.example.foreverfreedictionary.data.local.room.FavoriteDao
import com.example.foreverfreedictionary.domain.mapper.FavoriteMapper
import com.example.foreverfreedictionary.vo.Resource
import javax.inject.Inject

class FavoriteProvider @Inject constructor(
    private val local: FavoriteDao,
    private val cloud: FavoriteCloud,
    private val mapper: FavoriteMapper
) : BaseProvider() {
    suspend fun insertFavorite(favorite: FavoriteDictionary): Resource<Long> {
        return pushSources(
            databaseQuery = {
                local.insert(mapper.toData(favorite))
            }, cloudCall = {
                cloud.insert()
            }, mapper = {
                Resource.success(null)
            }
        )
    }

    fun getFavorite(): LiveData<Resource<List<FavoriteDictionary>>> {
        return Transformations.map(local.getFavorite()){
            Resource.success(it)
        }
    }

    suspend fun removeFavorite(query: String): Resource<Int>{
        return pushSources(
            databaseQuery = {
                local.delete(query)
            }, cloudCall = {
                cloud.delete(query)
            }, mapper = {
                Resource.success(null)
            }
        )
    }
}