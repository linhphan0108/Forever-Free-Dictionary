package com.example.foreverfreedictionary.ui.screen.result

import android.app.Application
import androidx.lifecycle.*
import com.example.foreverfreedictionary.domain.command.*
import com.example.foreverfreedictionary.ui.baseMVVM.BaseViewModel
import com.example.foreverfreedictionary.ui.mapper.DictionaryMapper
import com.example.foreverfreedictionary.ui.mapper.FavoriteMapper
import com.example.foreverfreedictionary.ui.mapper.ReminderMapper
import com.example.foreverfreedictionary.ui.model.DictionaryEntity
import com.example.foreverfreedictionary.ui.model.ReminderStates
import com.example.foreverfreedictionary.vo.Resource
import com.example.foreverfreedictionary.vo.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ResultActivityViewModel  @Inject constructor(
    application: Application,
    private val fetchDictionaryDataCommand: FetchDictionaryDataCommand,
    private val dictionaryMapper: DictionaryMapper,
    private val insertFavoriteCommand: InsertFavoriteCommand,
    private val removeFavoriteCommand: RemoveFavoriteCommand,
    private val favoriteMapper: FavoriteMapper,
    private val insertReminderCommand: InsertReminderCommand,
    private val deleteReminderCommand: DeleteReminderCommand,
    private val reminderMapper: ReminderMapper
    ) : BaseViewModel(application){

    private val _dictionaryMediatorLiveData = MediatorLiveData<Resource<DictionaryEntity>>()
    private var dictionaryResponse: LiveData<Resource<DictionaryEntity>>? = null
    val dictionary: LiveData<Resource<DictionaryEntity>> = _dictionaryMediatorLiveData

    private val _addFavoriteMutableLiveData = MutableLiveData<Resource<Long>>()
    private val _favoriteStatus = MutableLiveData<Boolean>()
    val favoriteStatus:LiveData<Boolean> = _favoriteStatus

    private val _reminderStatus = MutableLiveData<ReminderStates>()
    val reminderStatus:LiveData<ReminderStates> = _reminderStatus

    override fun onCleared() {
        clearDictionaryMediatorLiveData()
        super.onCleared()
    }

    fun query(query: String){
        _dictionaryMediatorLiveData.value = Resource.loading()
        //Connect to website
        viewModelScope.launch {
            //Working on UI thread
            //Use dispatcher to switch between application
            dictionaryResponse = withContext(Dispatchers.IO) {
                //Working on background thread
                fetchDictionaryDataCommand.query = query
                Transformations.map(fetchDictionaryDataCommand.execute()){
                    dictionaryMapper.fromDomain(it)
                }
            }
            //Working on UI thread
            clearDictionaryMediatorLiveData()
            _dictionaryMediatorLiveData.addSource(dictionaryResponse!!){resource ->
                val currentData = _dictionaryMediatorLiveData.value?.data
                val newData = resource.data
                if (currentData != null && newData != null && currentData.query == newData.query){
                    val currentFavorite = _favoriteStatus.value
                    val currentReminderStates = _reminderStatus.value
                    val newReminderStates = ReminderStates.from(newData.isReminded, newData.remindTime)
                    if (currentFavorite != newData.isFavorite){
                        _favoriteStatus.value = newData.isFavorite
                    }else if(currentReminderStates != newReminderStates){
                        _reminderStatus.value = newReminderStates
                    }
                }else {
                    _dictionaryMediatorLiveData.value = resource
                    _favoriteStatus.value = resource?.data?.isFavorite
                    resource?.data?.let {data ->
                        _reminderStatus.value = ReminderStates.from(data.isReminded, data.remindTime)
                    }
                }
            }
        }
    }

    private fun clearDictionaryMediatorLiveData(){
        dictionaryResponse?.let{_dictionaryMediatorLiveData.removeSource(it)}
    }

    fun onFavoriteButtonClicked(){
        _dictionaryMediatorLiveData.value?.data?.let {dictionaryEntity ->
            _favoriteStatus.value?.let { currentFavoriteStates ->
                if (currentFavoriteStates){
                    removeFavorite(dictionaryEntity)
                }else{
                    addFavorite(dictionaryEntity)
                }
            }
        }
    }

    fun onAddToMyVocabularyGroupClicked(){

    }

    private fun addFavorite(dictionaryEntity: DictionaryEntity) {
        viewModelScope.launch {
            insertFavoriteCommand.favorite = favoriteMapper.toDomain(dictionaryEntity, true)
            val deferred = async(Dispatchers.IO) {
                insertFavoriteCommand.execute()
            }
            _addFavoriteMutableLiveData.value = deferred.await()

        }
    }

    private fun removeFavorite(item: DictionaryEntity){
        viewModelScope.launch {
            removeFavoriteCommand.query = item.query
            withContext(Dispatchers.IO) {
                removeFavoriteCommand.execute()
            }
        }
    }

    fun onReminderButtonClicked(){
        _dictionaryMediatorLiveData.value?.data?.let {dictionaryEntity ->
            _reminderStatus.value?.let { currentReminderStates ->
                when(currentReminderStates){
                    ReminderStates.NOT_YET -> {
                        setReminder(dictionaryEntity)

                    }
                    ReminderStates.REMINDED -> {
                        setReminder(dictionaryEntity)
                    }
                    ReminderStates.ON_GOING -> {
                        deleteReminder(dictionaryEntity)
                    }
                }
            }
        }
    }

    private fun setReminder(item: DictionaryEntity){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                insertReminderCommand.reminder = reminderMapper.toDomain(item)
                insertReminderCommand.execute()
            }
        }
    }

    private fun deleteReminder(entity: DictionaryEntity){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                deleteReminderCommand.query = entity.query
                deleteReminderCommand.execute()
            }
        }
    }
}