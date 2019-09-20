package com.example.foreverfreedictionary.domain.mapper

import com.example.foreverfreedictionary.data.local.TblHistory
import java.sql.Date
import java.util.*

class HistoryMapper {
    fun toData(query: String): TblHistory {
        return TblHistory(query, Date(Calendar.getInstance().timeInMillis))
    }
}