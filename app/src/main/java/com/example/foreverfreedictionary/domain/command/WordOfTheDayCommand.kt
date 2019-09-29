package com.example.foreverfreedictionary.domain.command

import androidx.lifecycle.LiveData
import com.example.foreverfreedictionary.data.local.TblWordOfTheDay
import com.example.foreverfreedictionary.domain.provider.WordOfTheDayProvider
import com.example.foreverfreedictionary.vo.Resource
import javax.inject.Inject

class WordOfTheDayCommand @Inject constructor(
    private val wordOfTheDayProvider: WordOfTheDayProvider
) : BaseLiveDataCommand<TblWordOfTheDay>() {
    override suspend fun execute(): LiveData<Resource<TblWordOfTheDay>> {
        return wordOfTheDayProvider.fetchWordOfTheDay()
    }
}