package com.example.foreverfreedictionary.domain.provider

import androidx.lifecycle.LiveData
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
            dbCall = {
                local.getReminders()
            },cloudCall = {
                cloud.getReminders()
            },saveToDb = {

            })
    }
    suspend fun getRemindersInTime(timestamp: Long) : LiveData<Resource<List<Reminder>>>{
        return singleTruthSourceLiveData(
            dbCall = {
                local.getRemindersInTime(timestamp)
            },cloudCall = {
                cloud.getUnRemindedReminders()
            },saveToDb = {

            })
    }

    suspend fun countUnRemindedDictionaryReminder(timestamp: Long): Resource<Int>{
        return firstSource(
            dbCall = {
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
            Resource.success(null)
        })
    }

    suspend fun setReminded(queryList: List<String>) : LiveData<Resource<Int>>{
        return firstSourceLiveData(
            dbCall = {
                local.setReminded(queryList)
            }, cloudCall = {
                cloud.setReminded(queryList)
            }, mapper = {
                Resource.success(null)
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
                Resource.success(null)
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
                Resource.success(null)
            }
        )
    }
}