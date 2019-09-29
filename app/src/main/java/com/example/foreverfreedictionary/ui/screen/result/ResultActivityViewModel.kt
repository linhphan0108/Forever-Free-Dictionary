package com.example.foreverfreedictionary.ui.screen.result

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import com.example.foreverfreedictionary.domain.command.FetchDictionaryDataCommand
import com.example.foreverfreedictionary.ui.baseMVVM.BaseViewModel
import com.example.foreverfreedictionary.ui.mapper.DictionaryMapper
import com.example.foreverfreedictionary.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ResultActivityViewModel  @Inject constructor(
    application: Application,
    private val fetchDictionaryDataCommand: FetchDictionaryDataCommand,
    private val dictionaryMapper: DictionaryMapper
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
            _dictionaryMediatorLiveData.addSource(dictionaryResponse!!){
                _dictionaryMediatorLiveData.value = it
            }
        }
    }

    private fun clearDictionaryMediatorLiveData(){
        dictionaryResponse?.let{_dictionaryMediatorLiveData.removeSource(it)}
    }
}