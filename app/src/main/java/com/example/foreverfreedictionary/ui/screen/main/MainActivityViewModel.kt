package com.example.foreverfreedictionary.ui.screen.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.foreverfreedictionary.domain.command.FetchAutoCompletionCommand
import com.example.foreverfreedictionary.ui.baseMVVM.BaseViewModel
import com.example.foreverfreedictionary.ui.model.AutoCompletionEntity
import com.example.foreverfreedictionary.vo.Resource
import com.example.foreverfreedictionary.vo.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainActivityViewModel @Inject constructor(
    private val fetchAutoCompletionCommand: FetchAutoCompletionCommand
) : BaseViewModel() {

    private val _autoCompletionResponse = MutableLiveData<Resource<List<AutoCompletionEntity>>>()
    val autoCompletionResponse: LiveData<Resource<List<AutoCompletionEntity>>> = _autoCompletionResponse

    fun autocompleteQuery(query: String){
        uiScope.launch {
            //Working on UI thread

            //Use dispatcher to switch between context
            val deferred = async(Dispatchers.IO) {
                //Working on background thread
                fetchAutoCompletionCommand.query = query
                val data = fetchAutoCompletionCommand.execute()
                return@async when(data.status){
                    Status.LOADING -> {
                        Resource.loading()
                    }
                    Status.SUCCESS -> {
                        val mappedData = data.data!!.map {
                            AutoCompletionEntity(it)
                        }
                        Resource.success(mappedData)
                    }
                    Status.ERROR -> {
                        Resource.error(data.message)
                    }
                }
            }
            //Working on UI thread
            _autoCompletionResponse.value = deferred.await()
        }
    }
}