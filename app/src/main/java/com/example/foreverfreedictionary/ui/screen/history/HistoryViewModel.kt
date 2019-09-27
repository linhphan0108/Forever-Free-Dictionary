package com.example.foreverfreedictionary.ui.screen.history

import android.app.Application
import androidx.lifecycle.*
import com.example.foreverfreedictionary.domain.command.FetchHistoryCommand
import com.example.foreverfreedictionary.domain.command.InsertFavoriteCommand
import com.example.foreverfreedictionary.domain.command.InsertReminderCommand
import com.example.foreverfreedictionary.extensions.getTomorrow0Clock
import com.example.foreverfreedictionary.ui.baseMVVM.BaseViewModel
import com.example.foreverfreedictionary.ui.mapper.FavoriteMapper
import com.example.foreverfreedictionary.ui.model.HistoryEntity
import com.example.foreverfreedictionary.ui.mapper.HistoryMapper
import com.example.foreverfreedictionary.ui.mapper.ReminderMapper
import com.example.foreverfreedictionary.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.sql.Date
import javax.inject.Inject

class HistoryViewModel @Inject constructor(
    application: Application,
    private val historyCommand: FetchHistoryCommand,
    private val historyMapper: HistoryMapper,
    private val insertFavoriteCommand: InsertFavoriteCommand,
    private val favoriteMapper: FavoriteMapper,
    private val insertReminderCommand: InsertReminderCommand,
    private val reminderMapper: ReminderMapper
) : BaseViewModel(application) {

    private val _historyMediatorLiveData = MediatorLiveData<Resource<List<HistoryEntity>>>()
    private var _historyLiveData: LiveData<Resource<List<HistoryEntity>>>? = null
    val historyResponse = _historyMediatorLiveData

    private val _addFavoriteMutableLiveData = MutableLiveData<Resource<Long>>()
    val addFavoriteResponse = _addFavoriteMutableLiveData

    private val _setReminderMutableLiveData = MutableLiveData<Resource<Long>>()
    val setReminderResponse = _setReminderMutableLiveData

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

    fun addFavorite(item: HistoryEntity) {
        uiScope.launch {
            insertFavoriteCommand.favorite = favoriteMapper.toDomain(item)
            val deferred = async(Dispatchers.IO) {
                insertFavoriteCommand.execute()
            }

            _addFavoriteMutableLiveData.value = deferred.await()

        }
    }

    fun setReminder(item: HistoryEntity){
        val date = Date(System.currentTimeMillis()).getTomorrow0Clock()
        uiScope.launch {
            val deferred = async(Dispatchers.IO) {
                insertReminderCommand.reminder = reminderMapper.toDomain(item, date)
                insertReminderCommand.execute()
            }
            _setReminderMutableLiveData.value = deferred.await()
        }
    }
}