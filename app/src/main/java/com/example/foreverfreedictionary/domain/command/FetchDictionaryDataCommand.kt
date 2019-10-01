package com.example.foreverfreedictionary.domain.command

import androidx.lifecycle.LiveData
import com.example.foreverfreedictionary.data.local.model.DictionaryData
import com.example.foreverfreedictionary.domain.provider.DictionaryDataProvider
import com.example.foreverfreedictionary.vo.Resource
import javax.inject.Inject

class FetchDictionaryDataCommand @Inject constructor(
    private val dataProvider: DictionaryDataProvider
) : BaseLiveDataCommand<DictionaryData>() {

    lateinit var query: String

    override suspend fun execute(): LiveData<Resource<DictionaryData>> {
        return dataProvider.queryDictionaryData(query)
    }
}