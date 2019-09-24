package com.example.foreverfreedictionary.ui.screen.reminder

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.foreverfreedictionary.domain.command.FetchRemindersCommand
import com.example.foreverfreedictionary.domain.command.UpdateReminderStatusCommand
import com.example.foreverfreedictionary.ui.baseMVVM.BaseViewModel
import com.example.foreverfreedictionary.ui.mapper.ReminderMapper
import com.example.foreverfreedictionary.ui.model.ReminderEntity
import com.example.foreverfreedictionary.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.sql.Date
import javax.inject.Inject

class ReminderViewModel @Inject constructor(
    application: Application,
    private val fetchReminderInTimeCommand: FetchRemindersCommand,
    private val reminderMapper: ReminderMapper,
    private val updateReminderStatusCommand: UpdateReminderStatusCommand
): BaseViewModel(application) {

    private val _getReminderMediatorLiveData = MediatorLiveData<Resource<List<ReminderEntity>>>()
    private var _getReminderLiveData: LiveData<Resource<List<ReminderEntity>>>? = null
    val remindersResponse = _getReminderMediatorLiveData

    private val _insertReminderMediatorLiveData = MutableLiveData<Resource<Int>>()
    val insertReminderResponse = _insertReminderMediatorLiveData

    override fun onCleared() {
        clearGetReminderLiveData()
        super.onCleared()
    }

    fun getReminders(){
        uiScope.launch {
            val deferred = async(Dispatchers.IO) {
                Transformations.map(fetchReminderInTimeCommand.execute()){
                    reminderMapper.fromDomain(it)
                }
            }

            clearGetReminderLiveData()
            _getReminderLiveData = deferred.await()
            _getReminderMediatorLiveData.addSource(_getReminderLiveData!!){
                _getReminderMediatorLiveData.value = it
            }
        }
    }

    private fun clearGetReminderLiveData(){
        _getReminderLiveData?.let {
            _getReminderLiveData = null
        }
    }

    fun updateReminderStatus(query: String, isReminded: Boolean, time: Date){
        uiScope.launch {
            val deferred = async(Dispatchers.IO) {
                updateReminderStatusCommand.query = query
                updateReminderStatusCommand.isReminded = isReminded
                updateReminderStatusCommand.time = time
                updateReminderStatusCommand.execute()
            }

            val resource = deferred.await()
            _insertReminderMediatorLiveData .value = resource
        }
    }
}