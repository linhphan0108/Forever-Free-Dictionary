package com.example.foreverfreedictionary.domain.mapper

import com.example.foreverfreedictionary.data.local.TblFavorite
import com.example.foreverfreedictionary.data.local.model.FavoriteDictionary

class FavoriteMapper {
    fun toData(favorite: FavoriteDictionary): TblFavorite{
        return with(favorite){
            TblFavorite(query, word, soundBr, soundAme, ipaBr, ipaAme, isFavorite)
        }
    }
}