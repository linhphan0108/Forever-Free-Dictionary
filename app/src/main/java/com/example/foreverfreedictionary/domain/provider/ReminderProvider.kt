package com.example.foreverfreedictionary.domain.provider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.foreverfreedictionary.data.cloud.ReminderCloud
import com.example.foreverfreedictionary.data.local.TblReminder
import com.example.foreverfreedictionary.data.local.model.Reminder
import com.example.foreverfreedictionary.data.local.room.ReminderDao
import com.example.foreverfreedictionary.vo.Resource
import java.sql.Date
import javax.inject.Inject

class ReminderProvider @Inject constructor(
    private val local: ReminderDao,
    private val cloud: ReminderCloud
) : BaseProvider(){
    suspend fun getReminders() : LiveData<Resource<List<Reminder>>>{
        return singleTruthSourceLiveData(
            databaseQuery = {
                local.getReminders()
            },cloudCall = {
                cloud.getReminders()
            },saveCloudData = {

            })
    }
    suspend fun getRemindersInTime(timestamp: Long) : LiveData<Resource<List<Reminder>>>{
        return singleTruthSourceLiveData(
            databaseQuery = {
                local.getRemindersInTime(timestamp)
            },cloudCall = {
                cloud.getUnRemindedReminders()
            },saveCloudData = {

            })
    }

    suspend fun countUnRemindedDictionaryReminder(timestamp: Long): Resource<Int>{
        return firstSource(
            databaseQuery = {
                local.countRemindersInTime(timestamp)
            },cloudCall = {
                cloud.getUnRemindedReminders()
            },mapper = {
                Resource.error("no cloud implemented")
            })
    }

    suspend fun insertReminder(data: TblReminder) : Resource<Long>{
        return pushSources(databaseQuery = {
            local.insertReminder(data)
        }, cloudCall = {
            cloud.insertReminder()
        }, mapper = {
            it
        })
    }

    suspend fun setReminded(queryList: List<String>) : Resource<Int>{
        return firstSource(
            databaseQuery = {
                local.setReminded(queryList)
            }, cloudCall = {
                cloud.setReminded(queryList)
            }, mapper = {
                it
            }
        )
    }

    suspend fun updateReminder(query: String, isReminded: Boolean, time: Date): Resource<Int>{
        return pushSources(
            databaseQuery = {
                local.updateReminder(query, isReminded, time)
            }, cloudCall = {
                cloud.updateReminder(query, isReminded, time)
            }, mapper = {
                it
            }
        )
    }

    suspend fun deleteReminder(query: String): Resource<Int>{
        return pushSources(
            databaseQuery = {
                local.delete(query)
            }, cloudCall = {
                cloud.deleteReminder()
            }, mapper = {
                it
            }
        )
    }
}