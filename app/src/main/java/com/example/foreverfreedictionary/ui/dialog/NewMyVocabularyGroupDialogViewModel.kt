package com.example.foreverfreedictionary.ui.dialog

import android.app.Application
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import com.example.foreverfreedictionary.data.local.TblMyVocabularyGroup
import com.example.foreverfreedictionary.domain.command.InsertMyVocabularyGroupCommand
import com.example.foreverfreedictionary.ui.baseMVVM.BaseViewModel
import com.example.foreverfreedictionary.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NewMyVocabularyGroupDialogViewModel @Inject constructor(
    application: Application,
    private val insertMyVocabularyGroupCommand: InsertMyVocabularyGroupCommand) : BaseViewModel(application) {
    private val _insertMyVocabularyGroupMediatorLiveData = MediatorLiveData<Resource<Long>>()
    val insertMyVocabularyGroupLiveData = _insertMyVocabularyGroupMediatorLiveData

    fun insertNewMyVocabularyGroup(groupName: String){
        viewModelScope.launch {
            val resource = withContext(Dispatchers.IO){
                insertMyVocabularyGroupCommand.entity = TblMyVocabularyGroup(name = groupName)
                insertMyVocabularyGroupCommand.execute()
            }
            _insertMyVocabularyGroupMediatorLiveData.value = resource
        }
    }
}