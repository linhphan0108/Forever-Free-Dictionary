package com.example.foreverfreedictionary.ui.screen.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.foreverfreedictionary.domain.command.WordOfTheDayCommand
import com.example.foreverfreedictionary.ui.baseMVVM.BaseViewModel
import com.example.foreverfreedictionary.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

const val url = "https://www.ldoceonline.com/"
class HomeViewModel@Inject constructor(
    application: Application,
    private val wordOfTheDayCommand: WordOfTheDayCommand
) : BaseViewModel(application) {

    private val _wordOfTheDayResponse = MutableLiveData<Resource<String>>()
    val wordOfTheDay: LiveData<Resource<String>> = _wordOfTheDayResponse

    fun fetchWordOfTheDay() {
        uiScope.launch {
            //Working on UI thread
            //Use dispatcher to switch between context
            val deferred = async(Dispatchers.Default) {
                //Working on background thread
                wordOfTheDayCommand.execute()
            }
            //Working on UI thread
            _wordOfTheDayResponse.value = deferred.await()
        }
    }
}