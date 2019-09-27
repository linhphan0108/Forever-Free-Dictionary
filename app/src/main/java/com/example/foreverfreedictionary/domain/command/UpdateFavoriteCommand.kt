package com.example.foreverfreedictionary.domain.command

import androidx.lifecycle.LiveData
import com.example.foreverfreedictionary.domain.provider.DictionaryDataProvider
import com.example.foreverfreedictionary.vo.Resource
import javax.inject.Inject

class UpdateFavoriteCommand @Inject constructor(
    private val historyProvider: DictionaryDataProvider
) : BaseLiveDataCommand<Boolean>() {
    lateinit var query: String
    var isFavorite: Boolean = false

    override suspend fun execute(): LiveData<Resource<Boolean>> {
        return historyProvider.updateFavorite(query, isFavorite)
    }
}