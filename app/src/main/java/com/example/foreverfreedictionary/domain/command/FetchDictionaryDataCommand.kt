package com.example.foreverfreedictionary.domain.command

import com.example.foreverfreedictionary.domain.provider.DictionaryDataProvider
import com.example.foreverfreedictionary.vo.Resource
import javax.inject.Inject

class FetchDictionaryDataCommand @Inject constructor(
    private val dataProvider: DictionaryDataProvider
) : BaseCommand<String>() {

    lateinit var query: String

    override suspend fun execute(): Resource<String> {
        return dataProvider.queryDictionaryData(query)
    }
}