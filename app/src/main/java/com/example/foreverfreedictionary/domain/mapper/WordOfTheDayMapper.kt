package com.example.foreverfreedictionary.domain.mapper

import com.example.foreverfreedictionary.data.local.TblWordOfTheDay
import com.example.foreverfreedictionary.vo.Resource
import com.example.foreverfreedictionary.vo.Status
import java.sql.Date

class WordOfTheDayMapper {
    fun toData(date: Date, content: String): TblWordOfTheDay {
        return TblWordOfTheDay(date, content)
    }

    fun fromData(resource: TblWordOfTheDay): String{
        return resource.content
    }
}