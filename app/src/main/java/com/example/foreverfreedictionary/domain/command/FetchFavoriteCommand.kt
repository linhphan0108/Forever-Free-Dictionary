package com.example.foreverfreedictionary.domain.command

import androidx.lifecycle.LiveData
import com.example.foreverfreedictionary.data.local.model.FavoriteDictionary
import com.example.foreverfreedictionary.domain.provider.FavoriteProvider
import com.example.foreverfreedictionary.vo.Resource
import javax.inject.Inject

class FetchFavoriteCommand @Inject constructor(
    private val favoriteProvider: FavoriteProvider
) : BaseLiveDataCommand<List<FavoriteDictionary>>() {
    override suspend fun execute(): LiveData<Resource<List<FavoriteDictionary>>> {
        return favoriteProvider.getFavorite()
    }
}