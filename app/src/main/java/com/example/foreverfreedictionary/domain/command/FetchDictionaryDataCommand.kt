package com.example.foreverfreedictionary.domain.command

import androidx.lifecycle.LiveData
import com.example.foreverfreedictionary.data.local.TblDictionary
import com.example.foreverfreedictionary.domain.provider.DictionaryDataProvider
import com.example.foreverfreedictionary.vo.Resource
import javax.inject.Inject

class FetchDictionaryDataCommand @Inject constructor(
    private val dataProvider: DictionaryDataProvider
) : BaseLiveDataCommand<TblDictionary>() {

    lateinit var query: String

    override suspend fun execute(): LiveData<Resource<TblDictionary>> {
        return dataProvider.queryDictionaryData(query)
    }
}