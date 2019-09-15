package com.example.foreverfreedictionary.ui.screen.main

import android.util.Log
import com.example.foreverfreedictionary.domain.command.FetchAutoCompletionCommand
import com.example.foreverfreedictionary.ui.baseMVVM.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainActivityViewModel @Inject constructor(
    private val fetchAutoCompletionCommand: FetchAutoCompletionCommand
) : BaseViewModel() {

    fun autocompleteQuery(query: String){
        uiScope.launch {
            //Working on UI thread

            //Use dispatcher to switch between context
            val deferred = async(Dispatchers.IO) {
                //Working on background thread
                fetchAutoCompletionCommand.query = query
                fetchAutoCompletionCommand.execute()
            }
            val result = deferred.await()
            Log.d("linh534", result.toString())
            //Working on UI thread
        }
    }
}