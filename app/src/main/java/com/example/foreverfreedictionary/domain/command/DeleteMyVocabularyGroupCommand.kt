package com.example.foreverfreedictionary.domain.command

import com.example.foreverfreedictionary.domain.provider.MyVocabularyGroupProvider
import com.example.foreverfreedictionary.vo.Resource
import javax.inject.Inject

class DeleteMyVocabularyGroupCommand @Inject constructor(
    private val provider: MyVocabularyGroupProvider
) : BaseResourceCommand<Int>() {
    lateinit var groupName: String
    override suspend fun execute(): Resource<Int> {
        return provider.delete(groupName)
    }

}