package com.example.foreverfreedictionary.ui.dialog

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.example.foreverfreedictionary.data.local.TblMyVocabulary
import com.example.foreverfreedictionary.data.local.TblMyVocabularyGroup
import com.example.foreverfreedictionary.domain.command.AddMyVocabularyCommand
import com.example.foreverfreedictionary.domain.command.FetchMyVocabularyByQueryCommand
import com.example.foreverfreedictionary.domain.command.FetchMyVocabularyGroupCommand
import com.example.foreverfreedictionary.extensions.zipLiveData
import com.example.foreverfreedictionary.ui.baseMVVM.BaseViewModel
import com.example.foreverfreedictionary.ui.mapper.MyVocabularyGroupMapper
import com.example.foreverfreedictionary.ui.mapper.MyVocabularyMapper
import com.example.foreverfreedictionary.ui.model.DictionaryEntity
import com.example.foreverfreedictionary.ui.model.MyVocabularyEntity
import com.example.foreverfreedictionary.ui.model.MyVocabularyGroupEntity
import com.example.foreverfreedictionary.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MyVocabularyGroupPickerViewModel @Inject constructor(
    application: Application,
    private val fetchMyVocabularyGroupCommand: FetchMyVocabularyGroupCommand,
    private val addMyVocabularyCommand: AddMyVocabularyCommand,
    private val fetchMyVocabularyByQueryCommand: FetchMyVocabularyByQueryCommand,
    private val myVocabularyGroupMapper: MyVocabularyGroupMapper,
    private val myVocabularyMapper: MyVocabularyMapper
    )
    : BaseViewModel(application) {

    private val _getAllMyVocabularyGroupMediator = MediatorLiveData<Resource<List<MyVocabularyGroupEntity>>>()
    private var _getAllMyVocabularyGroupLiveData: LiveData<Resource<List<TblMyVocabularyGroup>>>? = null
    private var _getAllMyVocabularyLiveData: LiveData<Resource<List<TblMyVocabulary>>>? = null
    val myVocabularyGroup: LiveData<Resource<List<MyVocabularyGroupEntity>>> = _getAllMyVocabularyGroupMediator

    override fun onCleared() {
        super.onCleared()
        clearMyVocabularyGroupMediator()
    }

    fun getAllMyVocabularyGroup(query: String) {
        viewModelScope.launch {
            _getAllMyVocabularyGroupLiveData = withContext(Dispatchers.IO){
                fetchMyVocabularyGroupCommand.execute()
            }

            _getAllMyVocabularyLiveData = withContext(Dispatchers.IO){
                fetchMyVocabularyByQueryCommand.query = query
                fetchMyVocabularyByQueryCommand.execute()
            }

            clearMyVocabularyGroupMediator()
            _getAllMyVocabularyGroupMediator.zipLiveData(_getAllMyVocabularyGroupLiveData!!, _getAllMyVocabularyLiveData!!){ a, b ->
                myVocabularyGroupMapper.fromDomain(a, b)
            }
        }
    }

    fun addMyVocabularyToGroup(groupItem: MyVocabularyGroupEntity, dictionaryEntity: DictionaryEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                addMyVocabularyCommand.tblMyVocabulary = myVocabularyMapper.toData(groupItem.name, dictionaryEntity)
                addMyVocabularyCommand.execute()
            }
        }
    }

    private fun clearMyVocabularyGroupMediator(){
        _getAllMyVocabularyGroupLiveData?.let {
            _getAllMyVocabularyGroupMediator.removeSource(it)
        }
        _getAllMyVocabularyLiveData?.let {
            _getAllMyVocabularyGroupMediator.removeSource(it)
        }
    }
}