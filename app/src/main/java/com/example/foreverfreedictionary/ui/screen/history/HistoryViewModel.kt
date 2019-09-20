package com.example.foreverfreedictionary.ui.screen.history

import android.app.Application
import androidx.lifecycle.*
import com.example.foreverfreedictionary.domain.command.FetchHistoryCommand
import com.example.foreverfreedictionary.ui.baseMVVM.BaseViewModel
import com.example.foreverfreedictionary.ui.model.HistoryEntity
import com.example.foreverfreedictionary.ui.mapper.HistoryMapper
import com.example.foreverfreedictionary.vo.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class HistoryViewModel @Inject constructor(
    application: Application,
    private val historyCommand: FetchHistoryCommand,
    private val historyMapper: HistoryMapper) : BaseViewModel(application) {

    private val _historyMediatorLiveData = MediatorLiveData<Resource<List<HistoryEntity>>>()
    private var _historyLiveData: LiveData<Resource<List<HistoryEntity>>>? = null
    val historyResponse = _historyMediatorLiveData

    override fun onCleared() {
        clearHistorySources()
        super.onCleared()
    }

    fun getHistory(){
        uiScope.launch {
            val deferred = async {
                Transformations.map(historyCommand.execute()){
                    historyMapper.fromDomain(it)
                }
            }
            _historyLiveData = deferred.await()
            clearHistorySources()
            _historyMediatorLiveData.addSource(_historyLiveData!!){
                _historyMediatorLiveData.value = it
            }
        }
    }

    private fun clearHistorySources(){
        _historyLiveData?.let { _historyMediatorLiveData.removeSource(it) }
    }
}