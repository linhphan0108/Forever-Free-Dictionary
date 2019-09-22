package com.example.foreverfreedictionary.ui.screen.history

import android.app.Application
import androidx.lifecycle.*
import com.example.foreverfreedictionary.domain.command.FetchHistoryCommand
import com.example.foreverfreedictionary.domain.command.UpdateFavoriteCommand
import com.example.foreverfreedictionary.ui.baseMVVM.BaseViewModel
import com.example.foreverfreedictionary.ui.model.HistoryEntity
import com.example.foreverfreedictionary.ui.mapper.HistoryMapper
import com.example.foreverfreedictionary.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class HistoryViewModel @Inject constructor(
    application: Application,
    private val historyCommand: FetchHistoryCommand,
    private val historyMapper: HistoryMapper,
    private val updateFavoriteCommand: UpdateFavoriteCommand) : BaseViewModel(application) {

    private val _historyMediatorLiveData = MediatorLiveData<Resource<List<HistoryEntity>>>()
    private var _historyLiveData: LiveData<Resource<List<HistoryEntity>>>? = null
    val historyResponse = _historyMediatorLiveData

    private val _updateFavoriteMediatorLiveData = MediatorLiveData<Resource<Boolean>>()
    private var _updateFavoriteLiveData :LiveData<Resource<Boolean>>? = null
    val updateFavoriteResponse = _updateFavoriteMediatorLiveData

    override fun onCleared() {
        clearHistorySources()
        super.onCleared()
    }

    fun getHistory(){
        uiScope.launch {
            val deferred = async (Dispatchers.IO){
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

    fun updateFavorite(query: String, isFavorite: Boolean){
        uiScope.launch {
            updateFavoriteCommand.query = query
            updateFavoriteCommand.isFavorite = isFavorite
            val deferred = async(Dispatchers.IO) {
                updateFavoriteCommand.execute()
            }

            clearUpdateFavoriteLiveData()
            _updateFavoriteLiveData = deferred.await()
            _updateFavoriteMediatorLiveData.addSource(_updateFavoriteLiveData!!){
                _updateFavoriteMediatorLiveData.value = it
            }

        }
    }

    private fun clearUpdateFavoriteLiveData(){
        _updateFavoriteLiveData?.let { _updateFavoriteMediatorLiveData.removeSource(it) }
    }
}