package com.example.foreverfreedictionary.domain.command

import com.example.foreverfreedictionary.domain.provider.WordOfTheDayProvider
import com.example.foreverfreedictionary.vo.Resource
import javax.inject.Inject

class WordOfTheDayCommand @Inject constructor(
    private val wordOfTheDayProvider: WordOfTheDayProvider
) : BaseCommand<String>() {
    override suspend fun execute(): Resource<String> {
        return wordOfTheDayProvider.fetchWordOfTheDay()
    }
}