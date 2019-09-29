package com.example.foreverfreedictionary.ui.screen.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.example.foreverfreedictionary.domain.command.WordOfTheDayCommand
import com.example.foreverfreedictionary.ui.baseMVVM.BaseViewModel
import com.example.foreverfreedictionary.ui.mapper.WordOfTheDayMapper
import com.example.foreverfreedictionary.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

const val url = "https://www.ldoceonline.com/"
class HomeViewModel@Inject constructor(
    application: Application,
    private val wordOfTheDayCommand: WordOfTheDayCommand,
    private val wordOfTheDayMapper: WordOfTheDayMapper
) : BaseViewModel(application) {

    private val _wordOfTheDayMediatorLiveData = MediatorLiveData<Resource<String>>()
    private var wordOfTheDayResponse: LiveData<Resource<String>>? = null
    val wordOfTheDay: LiveData<Resource<String>> = _wordOfTheDayMediatorLiveData

    override fun onCleared() {
        clearWordOfTheDayMediatorLiveData()
        super.onCleared()
    }

    fun fetchWordOfTheDay() {
        viewModelScope.launch {
            //Working on UI thread
            //Use dispatcher to switch between application
            wordOfTheDayResponse = withContext (Dispatchers.IO) {
                //Working on background thread
                Transformations.map(wordOfTheDayCommand.execute()){
                    wordOfTheDayMapper.fromDomain(it)
                }
            }
            //Working on UI thread
            clearWordOfTheDayMediatorLiveData()
            _wordOfTheDayMediatorLiveData.addSource(wordOfTheDayResponse!!){
                _wordOfTheDayMediatorLiveData.value = it
            }
        }
    }

    private fun clearWordOfTheDayMediatorLiveData(){
        wordOfTheDayResponse?.let { _wordOfTheDayMediatorLiveData.removeSource(it) }
    }
}