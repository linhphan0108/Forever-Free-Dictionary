package com.example.foreverfreedictionary.ui.screen.gallery

import android.app.Application
import androidx.lifecycle.*
import com.example.foreverfreedictionary.domain.command.FetchFavoriteCommand
import com.example.foreverfreedictionary.domain.command.InsertFavoriteCommand
import com.example.foreverfreedictionary.domain.command.InsertReminderCommand
import com.example.foreverfreedictionary.domain.command.RemoveFavoriteCommand
import com.example.foreverfreedictionary.ui.baseMVVM.BaseViewModel
import com.example.foreverfreedictionary.ui.mapper.FavoriteMapper
import com.example.foreverfreedictionary.ui.mapper.ReminderMapper
import com.example.foreverfreedictionary.ui.model.FavoriteEntity
import com.example.foreverfreedictionary.vo.Resource
import com.example.foreverfreedictionary.vo.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.sql.Date
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(application: Application,
                                            private val favoriteCommand: FetchFavoriteCommand,
                                            private val favoriteMapper: FavoriteMapper,
                                            private val insertFavoriteCommand: InsertFavoriteCommand,
                                            private val removeFavoriteCommand: RemoveFavoriteCommand,
                                            private val insertReminderCommand: InsertReminderCommand,
                                            private val reminderMapper: ReminderMapper
) : BaseViewModel(application) {

    private val _favoriteMediatorLiveData = MediatorLiveData<Resource<List<FavoriteEntity>>>()
    private var _favoriteLiveData: LiveData<Resource<List<FavoriteEntity>>>? = null
    val favoriteResponse = _favoriteMediatorLiveData

    private val _insertFavoriteMutableLiveData = MutableLiveData<Resource<Long>>()
    val insertFavoriteResponse = _insertFavoriteMutableLiveData

    private val _removeFavoriteMutableLiveData = MutableLiveData<Resource<FavoriteEntity>>()
    val removeFavoriteResponse = _removeFavoriteMutableLiveData

    private val _insertReminderMediatorLiveData = MediatorLiveData<Resource<Long>>()
    private var _insertReminderLiveData: LiveData<Resource<Long>>? = null
    val insertReminderResponse = _insertReminderMediatorLiveData

    override fun onCleared() {
        clearHistorySources()
        clearInsertReminderLiveData()
        super.onCleared()
    }

    fun getFavorites(){
        uiScope.launch {
            val deferred = async (Dispatchers.IO){
                return@async Transformations.map(favoriteCommand.execute()){
                    favoriteMapper.fromDomain(it)
                }
            }
            _favoriteLiveData = deferred.await()
            clearHistorySources()
            _favoriteMediatorLiveData.addSource(_favoriteLiveData!!){
                _favoriteMediatorLiveData.value = it
            }
        }
    }

    private fun clearHistorySources(){
        _favoriteLiveData?.let { _favoriteMediatorLiveData.removeSource(it) }
    }

    fun removeFavorite(item: FavoriteEntity){
        uiScope.launch {
            removeFavoriteCommand.query = item.query
            val deferred = async(Dispatchers.IO) {
                val resource = removeFavoriteCommand.execute()
                when(resource.status){
                    Status.LOADING -> {Resource.loading()}
                    Status.ERROR -> {Resource.error(resource.message)}
                    Status.SUCCESS -> {
                        Resource.success(item)
                    }
                }
            }
            _removeFavoriteMutableLiveData.value = deferred.await()
        }
    }

    fun insertFavorite(item: FavoriteEntity){
        uiScope.launch {
            val deferred = async (Dispatchers.IO) {
                insertFavoriteCommand.favorite = favoriteMapper.toDomain(item)
                insertFavoriteCommand.execute()
            }
            _insertFavoriteMutableLiveData.value = deferred.await()
        }
    }

    fun setReminder(item: FavoriteEntity, remindTime: Date){
        uiScope.launch {
            val deferred = async(Dispatchers.IO) {
                insertReminderCommand.reminder = reminderMapper.toDomain(item, remindTime)
                insertReminderCommand.execute()
            }

            clearInsertReminderLiveData()
            _insertReminderLiveData = deferred.await()
            _insertReminderMediatorLiveData.addSource(_insertReminderLiveData!!){
                _insertReminderMediatorLiveData.value = it
            }
        }
    }

    private fun clearInsertReminderLiveData(){
        _insertReminderLiveData?.let {
            _insertReminderLiveData = null
        }
    }
}