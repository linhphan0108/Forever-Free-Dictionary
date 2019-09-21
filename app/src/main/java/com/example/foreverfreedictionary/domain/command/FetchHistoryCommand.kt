package com.example.foreverfreedictionary.domain.command

import androidx.lifecycle.LiveData
import com.example.foreverfreedictionary.data.local.model.DictionaryHistory
import com.example.foreverfreedictionary.domain.provider.HistoryProvider
import com.example.foreverfreedictionary.vo.Resource
import javax.inject.Inject

class FetchHistoryCommand @Inject constructor(
    private val historyProvider: HistoryProvider
) : BaseCommand<List<DictionaryHistory>>() {

    override suspend fun execute(): LiveData<Resource<List<DictionaryHistory>>> {
        return historyProvider.getHistory()
    }
}