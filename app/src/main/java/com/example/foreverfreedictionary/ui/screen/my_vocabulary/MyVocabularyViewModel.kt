package com.example.foreverfreedictionary.ui.screen.my_vocabulary

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.example.foreverfreedictionary.domain.command.DeleteMyVocabularyCommand
import com.example.foreverfreedictionary.domain.command.FetchMyVocabularyByGroupCommand
import com.example.foreverfreedictionary.ui.baseMVVM.BaseViewModel
import com.example.foreverfreedictionary.ui.mapper.MyVocabularyMapper
import com.example.foreverfreedictionary.ui.model.MyVocabularyEntity
import com.example.foreverfreedictionary.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MyVocabularyViewModel @Inject constructor(
    application: Application,
    private val fetchMyVocabularyCommand: FetchMyVocabularyByGroupCommand,
    private val deleteMyVocabularyCommand: DeleteMyVocabularyCommand,
    private val myVocabularyMapper: MyVocabularyMapper
)
    : BaseViewModel(application) {

    private val _getAllMyVocabularyMediator = MediatorLiveData<Resource<List<MyVocabularyEntity>>>()
    private var _getAllMyVocabularyLiveData: LiveData<Resource<List<MyVocabularyEntity>>>? = null
    val myVocabulary: LiveData<Resource<List<MyVocabularyEntity>>> = _getAllMyVocabularyMediator


    override fun onCleared() {
        super.onCleared()
        clearMyVocabularyGroupMediator()
    }

    fun getAllMyVocabularyOfGroup(groupName: String){
        viewModelScope.launch {
            _getAllMyVocabularyLiveData = withContext(Dispatchers.IO){
                fetchMyVocabularyCommand.groupName = groupName
                Transformations.map(fetchMyVocabularyCommand.execute()) {resource ->
                    myVocabularyMapper.fromData(resource)
                }
            }
            clearMyVocabularyGroupMediator()
            _getAllMyVocabularyMediator.addSource(_getAllMyVocabularyLiveData!!){
                _getAllMyVocabularyMediator.value = it
            }
        }
    }

    fun deleteMyVocabulary(query: String, groupName: String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                deleteMyVocabularyCommand.query = query
                deleteMyVocabularyCommand.groupName = groupName
                deleteMyVocabularyCommand.execute()
            }
        }
    }

    private fun clearMyVocabularyGroupMediator(){
        _getAllMyVocabularyLiveData?.let {
            _getAllMyVocabularyMediator.removeSource(it)
        }
    }
}