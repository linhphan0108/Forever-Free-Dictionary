package com.example.foreverfreedictionary.domain.command

import com.example.foreverfreedictionary.data.local.TblMyVocabularyGroup
import com.example.foreverfreedictionary.domain.provider.MyVocabularyGroupProvider
import com.example.foreverfreedictionary.vo.Resource
import javax.inject.Inject

class InsertMyVocabularyGroupCommand @Inject constructor(
    private val provider: MyVocabularyGroupProvider
) : BaseResourceCommand<Long>() {
    lateinit var entity: TblMyVocabularyGroup
    override suspend fun execute(): Resource<Long> {
        return provider.insert(entity)
    }

}