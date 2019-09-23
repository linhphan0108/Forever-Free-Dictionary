package com.example.foreverfreedictionary.domain.command

import androidx.lifecycle.LiveData
import com.example.foreverfreedictionary.data.local.TblDictionary
import com.example.foreverfreedictionary.domain.provider.DictionaryDataProvider
import com.example.foreverfreedictionary.vo.Resource
import javax.inject.Inject

class FetchFavoriteCommand @Inject constructor(
    private val dictionaryDataProvider: DictionaryDataProvider
) : BaseCommand<List<TblDictionary>>() {
    override suspend fun execute(): LiveData<Resource<List<TblDictionary>>> {
        return dictionaryDataProvider.getFavorite()
    }
}