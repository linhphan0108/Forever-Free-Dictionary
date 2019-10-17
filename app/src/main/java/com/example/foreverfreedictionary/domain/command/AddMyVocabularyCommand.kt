package com.example.foreverfreedictionary.domain.command

import com.example.foreverfreedictionary.data.local.TblMyVocabulary
import com.example.foreverfreedictionary.domain.provider.MyVocabularyProvider
import com.example.foreverfreedictionary.vo.Resource
import javax.inject.Inject

class AddMyVocabularyCommand @Inject constructor(
    private val provider: MyVocabularyProvider
) : BaseResourceCommand<Long>() {
    lateinit var tblMyVocabulary: TblMyVocabulary
    override suspend fun execute(): Resource<Long> {
        return provider.insert(tblMyVocabulary)
    }

}