package com.example.foreverfreedictionary.domain.command

import com.example.foreverfreedictionary.domain.provider.MyVocabularyProvider
import com.example.foreverfreedictionary.vo.Resource
import javax.inject.Inject

class DeleteMyVocabularyCommand @Inject constructor(
    private val provider: MyVocabularyProvider
) : BaseResourceCommand<Int>() {
    lateinit var query: String
    lateinit var groupName: String
    override suspend fun execute(): Resource<Int> {
        return provider.delete(query, groupName)
    }

}