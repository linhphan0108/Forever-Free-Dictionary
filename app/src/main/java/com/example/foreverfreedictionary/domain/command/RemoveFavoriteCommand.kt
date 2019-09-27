package com.example.foreverfreedictionary.domain.command

import com.example.foreverfreedictionary.domain.provider.FavoriteProvider
import com.example.foreverfreedictionary.vo.Resource
import javax.inject.Inject

class RemoveFavoriteCommand @Inject constructor(
    private val favoriteProvider: FavoriteProvider
) : BaseResourceCommand<Int>() {
    lateinit var query: String

    override suspend fun execute(): Resource<Int> {
        return favoriteProvider.removeFavorite(query)
    }
}