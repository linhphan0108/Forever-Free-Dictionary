package com.example.foreverfreedictionary.ui.mapper

import com.example.foreverfreedictionary.data.local.TblReminder
import com.example.foreverfreedictionary.data.local.model.Reminder
import com.example.foreverfreedictionary.ui.model.FavoriteEntity
import com.example.foreverfreedictionary.ui.model.ReminderEntity
import com.example.foreverfreedictionary.vo.Resource
import com.example.foreverfreedictionary.vo.Status
import java.sql.Date

class ReminderMapper {
    fun toDomain(item: FavoriteEntity, remindTime: Date): TblReminder{
        return with(item){
            TblReminder(query, word,  soundBr, soundAme, ipaBr, ipaAme, false, remindTime)
        }
    }

    fun toDomain(item: ReminderEntity, remindTime: Date): TblReminder{
        return with(item){
            TblReminder(query, word,  soundBr, soundAme, ipaBr, ipaAme, false, remindTime)
        }
    }

    fun fromDomain(resource: Resource<List<Reminder>>) : Resource<List<ReminderEntity>> {
        return when(resource.status){
            Status.LOADING -> {Resource.loading()}
            Status.ERROR -> {Resource.error(resource.message)}
            Status.SUCCESS ->{
                if (resource.data == null){
                    Resource.success(null)
                }else {
                    fromDomain(resource.data)
                }
            }
        }
    }

    fun fromDomain(reminders: List<Reminder>?) : Resource<List<ReminderEntity>> {
        return Resource.success(reminders?.map {
            with(it){
                ReminderEntity(query, word,  soundBr, soundAme, ipaBr, ipaAme, isReminded, remindTime)
            }
        })
    }
}