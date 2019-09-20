package com.example.foreverfreedictionary.ui.screen.result

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.foreverfreedictionary.domain.command.FetchDictionaryDataCommand
import com.example.foreverfreedictionary.ui.baseMVVM.BaseViewModel
import com.example.foreverfreedictionary.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class ResultActivityViewModel  @Inject constructor(
    application: Application,
    private val fetchDictionaryDataCommand: FetchDictionaryDataCommand
) : BaseViewModel(application){

    private val _dictionaryMediatorLiveData = MediatorLiveData<Resource<String>>()
    private var dictionaryResponse: LiveData<Resource<String>>? = null
    val dictionary: LiveData<Resource<String>> = _dictionaryMediatorLiveData

    override fun onCleared() {
        clearDictionaryMediatorLiveData()
        super.onCleared()
    }

    fun query(query: String){
        _dictionaryMediatorLiveData.value = Resource.loading()
        //Connect to website
        uiScope.launch {
            //Working on UI thread
            //Use dispatcher to switch between context
            val deferred = async(Dispatchers.Default) {
                //Working on background thread
                fetchDictionaryDataCommand.query = query
                fetchDictionaryDataCommand.execute()
            }
            //Working on UI thread
            clearDictionaryMediatorLiveData()
            dictionaryResponse = deferred.await()
            _dictionaryMediatorLiveData.addSource(dictionaryResponse!!){
                _dictionaryMediatorLiveData.value = it
            }
        }
    }

    private fun clearDictionaryMediatorLiveData(){
        dictionaryResponse?.let{_dictionaryMediatorLiveData.removeSource(it)}
    }
}