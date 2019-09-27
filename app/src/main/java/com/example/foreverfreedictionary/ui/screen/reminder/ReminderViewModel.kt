package com.example.foreverfreedictionary.ui.screen.reminder

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.foreverfreedictionary.domain.command.DeleteReminderCommand
import com.example.foreverfreedictionary.domain.command.FetchRemindersCommand
import com.example.foreverfreedictionary.domain.command.InsertReminderCommand
import com.example.foreverfreedictionary.domain.command.UpdateReminderStatusCommand
import com.example.foreverfreedictionary.ui.baseMVVM.BaseViewModel
import com.example.foreverfreedictionary.ui.mapper.ReminderMapper
import com.example.foreverfreedictionary.ui.model.ReminderEntity
import com.example.foreverfreedictionary.vo.Resource
import com.example.foreverfreedictionary.vo.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.sql.Date
import javax.inject.Inject

class ReminderViewModel @Inject constructor(
    application: Application,
    private val fetchReminderInTimeCommand: FetchRemindersCommand,
    private val reminderMapper: ReminderMapper,
    private val insertReminderCommand: InsertReminderCommand,
    private val updateReminderStatusCommand: UpdateReminderStatusCommand,
    private val deleteReminderCommand: DeleteReminderCommand
): BaseViewModel(application) {

    private val _getReminderMediatorLiveData = MediatorLiveData<Resource<List<ReminderEntity>>>()
    private var _getReminderLiveData: LiveData<Resource<List<ReminderEntity>>>? = null
    val remindersResponse = _getReminderMediatorLiveData

    private val _insertReminderMutableLiveData = MutableLiveData<Resource<Int>>()
    val insertReminderResponse = _insertReminderMutableLiveData

    private val _setReminderMutableLiveData = MutableLiveData<Resource<Long>>()
    val setReminderResponse = _setReminderMutableLiveData

    private val _deleteReminderMediatorLiveData = MutableLiveData<Resource<ReminderEntity>>()
    val deleteReminderResponse = _deleteReminderMediatorLiveData

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
            _insertReminderMutableLiveData .value = resource
        }
    }

    fun deleteReminder(entity: ReminderEntity){
        uiScope.launch {
            val deferred = async (Dispatchers.IO){
                deleteReminderCommand.query = entity.query
                val resource = deleteReminderCommand.execute()
                when(resource.status){
                    Status.LOADING -> {Resource.loading()}
                    Status.ERROR -> {Resource.error(resource.message)}
                    Status.SUCCESS -> {
                        Resource.success(entity)
                    }
                }
            }
            _deleteReminderMediatorLiveData.value = deferred.await()
        }
    }

    fun setReminder(item: ReminderEntity){
        uiScope.launch {
            val deferred = async(Dispatchers.IO) {
                insertReminderCommand.reminder = reminderMapper.toDomain(item, item.remindTime)
                insertReminderCommand.execute()
            }
            _setReminderMutableLiveData.value = deferred.await()
        }
    }
}