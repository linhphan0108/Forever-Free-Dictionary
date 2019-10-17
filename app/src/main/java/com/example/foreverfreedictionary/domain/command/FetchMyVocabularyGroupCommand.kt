package com.example.foreverfreedictionary.domain.command

import androidx.lifecycle.LiveData
import com.example.foreverfreedictionary.data.local.TblMyVocabularyGroup
import com.example.foreverfreedictionary.domain.provider.MyVocabularyGroupProvider
import com.example.foreverfreedictionary.vo.Resource
import javax.inject.Inject

class FetchMyVocabularyGroupCommand @Inject constructor(
    private val provider: MyVocabularyGroupProvider
) : BaseLiveDataCommand<List<TblMyVocabularyGroup>>() {
    override suspend fun execute(): LiveData<Resource<List<TblMyVocabularyGroup>>> {
        return provider.getAllMyVocabularyGroup()
    }
}