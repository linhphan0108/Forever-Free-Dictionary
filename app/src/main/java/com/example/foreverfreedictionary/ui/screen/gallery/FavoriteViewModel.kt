package com.example.foreverfreedictionary.ui.screen.gallery

import android.app.Application
import androidx.lifecycle.*
import com.example.foreverfreedictionary.domain.command.FetchFavoriteCommand
import com.example.foreverfreedictionary.domain.command.UpdateFavoriteCommand
import com.example.foreverfreedictionary.ui.baseMVVM.BaseViewModel
import com.example.foreverfreedictionary.ui.mapper.FavoriteMapper
import com.example.foreverfreedictionary.ui.model.FavoriteEntity
import com.example.foreverfreedictionary.ui.model.HistoryEntity
import com.example.foreverfreedictionary.vo.Resource
import com.example.foreverfreedictionary.vo.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(application: Application,
        private val favoriteCommand: FetchFavoriteCommand,
        private val favoriteMapper: FavoriteMapper,
        private val updateFavoriteCommand: UpdateFavoriteCommand
) : BaseViewModel(application) {

    private val _favoriteMediatorLiveData = MediatorLiveData<Resource<List<FavoriteEntity>>>()
    private var _favoriteLiveData: LiveData<Resource<List<FavoriteEntity>>>? = null
    val favoriteResponse = _favoriteMediatorLiveData

    private val _updateFavoriteMediatorLiveData = MediatorLiveData<Resource<FavoriteEntity>>()
    private var _updateFavoriteLiveData :LiveData<Resource<Boolean>>? = null
    val updateFavoriteResponse = _updateFavoriteMediatorLiveData

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

    fun updateFavorite(item: FavoriteEntity, isFavorite: Boolean){
        uiScope.launch {
            updateFavoriteCommand.query = item.query
            updateFavoriteCommand.isFavorite = isFavorite
            val deferred = async(Dispatchers.IO) {
                updateFavoriteCommand.execute()
            }

            clearUpdateFavoriteLiveData()
            _updateFavoriteLiveData = deferred.await()
            _updateFavoriteMediatorLiveData.addSource(_updateFavoriteLiveData!!){resource ->
                val data = when(resource.status){
                    Status.LOADING -> {Resource.loading()}
                    Status.ERROR -> {Resource.error(resource.message)}
                    Status.SUCCESS -> {
                        val updatedItem = with(item){
                            FavoriteEntity(query, word, ipaBr, ipaAme, isFavorite, lastAccess)
                        }
                        Resource.success(updatedItem)
                    }
                }
                _updateFavoriteMediatorLiveData.value = data
            }

        }
    }

    private fun clearUpdateFavoriteLiveData(){
        _updateFavoriteLiveData?.let { _updateFavoriteMediatorLiveData.removeSource(it) }
    }
}