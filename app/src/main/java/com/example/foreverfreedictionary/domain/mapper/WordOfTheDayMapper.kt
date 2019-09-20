package com.example.foreverfreedictionary.domain.mapper

import com.example.foreverfreedictionary.data.local.TblWordOfTheDay
import com.example.foreverfreedictionary.vo.Resource
import com.example.foreverfreedictionary.vo.Status
import java.sql.Date

class WordOfTheDayMapper {
    fun toData(date: Date, content: String): TblWordOfTheDay {
        return TblWordOfTheDay(date, content)
    }

    fun fromData(resource: Resource<TblWordOfTheDay>): Resource<String>{
        return when(resource.status){
            Status.LOADING -> {Resource.loading()}
            Status.ERROR -> {Resource.error(resource.message)}
            Status.SUCCESS -> {Resource.success(resource.data!!.content)}
        }
    }
}