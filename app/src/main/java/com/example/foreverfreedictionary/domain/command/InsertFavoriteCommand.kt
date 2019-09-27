package com.example.foreverfreedictionary.domain.command

import com.example.foreverfreedictionary.data.local.model.FavoriteDictionary
import com.example.foreverfreedictionary.domain.provider.FavoriteProvider
import com.example.foreverfreedictionary.vo.Resource
import javax.inject.Inject

class InsertFavoriteCommand @Inject constructor(
    private val favoriteProvider: FavoriteProvider
): BaseResourceCommand<Long>() {
    lateinit var favorite: FavoriteDictionary
    override suspend fun execute(): Resource<Long> {
        return favoriteProvider.insertFavorite(favorite)
    }
}