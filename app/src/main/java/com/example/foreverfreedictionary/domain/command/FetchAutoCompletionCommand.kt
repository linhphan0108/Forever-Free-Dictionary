package com.example.foreverfreedictionary.domain.command

import com.example.foreverfreedictionary.domain.provider.AutoCompletionProvider
import com.example.foreverfreedictionary.vo.Resource
import javax.inject.Inject

class FetchAutoCompletionCommand @Inject constructor(
    private val autoCompletionProvider: AutoCompletionProvider
) : BaseCommand<List<String>>() {

    lateinit var query: String

    override suspend fun execute(): Resource<List<String>> = autoCompletionProvider.fetchAutoCompletion(query)
}