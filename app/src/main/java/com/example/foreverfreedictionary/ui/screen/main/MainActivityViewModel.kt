package com.example.foreverfreedictionary.ui.screen.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import com.example.foreverfreedictionary.domain.command.FetchAutoCompletionCommand
import com.example.foreverfreedictionary.ui.baseMVVM.BaseViewModel
import com.example.foreverfreedictionary.ui.model.AutoCompletionEntity
import com.example.foreverfreedictionary.ui.mapper.AutoCompletionMapper
import com.example.foreverfreedictionary.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainActivityViewModel @Inject constructor(
    private val fetchAutoCompletionCommand: FetchAutoCompletionCommand,
    private val autoCompletionMapper: AutoCompletionMapper,
    application: Application
) : BaseViewModel(application) {

    private val _autoCompletionMediatorLiveData = MediatorLiveData<Resource<List<AutoCompletionEntity>>>()
    private var autoCompletionResponse: LiveData<Resource<List<AutoCompletionEntity>>>? = null
    val autoCompletion: LiveData<Resource<List<AutoCompletionEntity>>> = _autoCompletionMediatorLiveData

    override fun onCleared() {
        clearAutoCompletionMediatorLiveData()
        super.onCleared()
    }

    fun autocompleteQuery(query: String){
        uiScope.launch {
            //Working on UI thread

            //Use dispatcher to switch between application
            val deferred = async(Dispatchers.IO) {
                //Working on background thread
                fetchAutoCompletionCommand.query = query
                return@async Transformations.map(fetchAutoCompletionCommand.execute()){resource ->
                    autoCompletionMapper.fromDomain(resource)
                }
            }
            //Working on UI thread
            autoCompletionResponse = deferred.await()
            clearAutoCompletionMediatorLiveData()
            _autoCompletionMediatorLiveData.addSource(autoCompletionResponse!!){
                _autoCompletionMediatorLiveData.value = it
            }
        }
    }

    private fun clearAutoCompletionMediatorLiveData(){
        autoCompletionResponse?.let { _autoCompletionMediatorLiveData.removeSource(it) }
    }
}