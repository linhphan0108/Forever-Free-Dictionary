package com.example.foreverfreedictionary.ui.screen.my_vocabulary

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.example.foreverfreedictionary.data.local.TblMyVocabularyGroup
import com.example.foreverfreedictionary.domain.command.DeleteMyVocabularyGroupCommand
import com.example.foreverfreedictionary.domain.command.FetchMyVocabularyGroupCommand
import com.example.foreverfreedictionary.domain.command.InsertMyVocabularyGroupCommand
import com.example.foreverfreedictionary.ui.baseMVVM.BaseViewModel
import com.example.foreverfreedictionary.ui.mapper.MyVocabularyGroupMapper
import com.example.foreverfreedictionary.ui.model.MyVocabularyGroupEntity
import com.example.foreverfreedictionary.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MyVocabularyGroupViewModel @Inject constructor(
    application: Application,
    private val fetchMyVocabularyGroupCommand: FetchMyVocabularyGroupCommand,
    private val deleteMyVocabularyGroupCommand: DeleteMyVocabularyGroupCommand,
    private val myVocabularyGroupMapper: MyVocabularyGroupMapper)
    : BaseViewModel(application) {

    private val _getAllMyVocabularyGroupMediator = MediatorLiveData<Resource<List<MyVocabularyGroupEntity>>>()
    private var _getAllMyVocabularyGroupLiveData: LiveData<Resource<List<MyVocabularyGroupEntity>>>? = null
    val myVocabularyGroup: LiveData<Resource<List<MyVocabularyGroupEntity>>> = _getAllMyVocabularyGroupMediator


    override fun onCleared() {
        super.onCleared()
        clearMyVocabularyGroupMediator()
    }

    fun getAllMyVocabularyGroup(){
        viewModelScope.launch {
            _getAllMyVocabularyGroupLiveData = withContext(Dispatchers.IO){
                Transformations.map(fetchMyVocabularyGroupCommand.execute()) {resource ->
                    myVocabularyGroupMapper.fromDomain(resource)
                }
            }
            clearMyVocabularyGroupMediator()
            _getAllMyVocabularyGroupMediator.addSource(_getAllMyVocabularyGroupLiveData!!){
                _getAllMyVocabularyGroupMediator.value = it
            }
        }
    }



    fun deleteMyVocabularyGroup(groupName: String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                deleteMyVocabularyGroupCommand.groupName = groupName
                deleteMyVocabularyGroupCommand.execute()
            }
        }
    }

    private fun clearMyVocabularyGroupMediator(){
        _getAllMyVocabularyGroupLiveData?.let {
            _getAllMyVocabularyGroupMediator.removeSource(it)
        }
    }
}