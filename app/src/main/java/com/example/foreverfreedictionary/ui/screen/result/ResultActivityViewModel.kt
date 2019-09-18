package com.example.foreverfreedictionary.ui.screen.result

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val _queryResponse = MutableLiveData<Resource<String>>()
    val queryResponse: LiveData<Resource<String>> = _queryResponse

    fun query(query: String){
        _queryResponse.value = Resource.loading()
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
            val data = deferred.await()
            _queryResponse.value = data
        }
    }
}