package com.example.foreverfreedictionary.domain.command

import androidx.lifecycle.LiveData
import com.example.foreverfreedictionary.data.local.TblMyVocabulary
import com.example.foreverfreedictionary.domain.provider.MyVocabularyProvider
import com.example.foreverfreedictionary.vo.Resource
import javax.inject.Inject

class FetchMyVocabularyByQueryCommand @Inject constructor(
    private val provider: MyVocabularyProvider
) : BaseLiveDataCommand<List<TblMyVocabulary>>() {
    lateinit var query: String
    override suspend fun execute(): LiveData<Resource<List<TblMyVocabulary>>> {
        return provider.getAllMyVocabularyByQuery(query)
    }
}