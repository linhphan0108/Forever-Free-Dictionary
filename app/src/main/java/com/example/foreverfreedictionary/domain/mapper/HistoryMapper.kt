package com.example.foreverfreedictionary.domain.mapper

import com.example.foreverfreedictionary.data.cloud.model.Dictionary
import com.example.foreverfreedictionary.data.local.TblHistory
import java.sql.Date
import java.util.*

class HistoryMapper {
    fun toData(cloudData: Dictionary): TblHistory {
        return TblHistory(cloudData.query, cloudData.word, Date(Calendar.getInstance().timeInMillis))
    }
}