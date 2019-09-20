package com.example.foreverfreedictionary.domain.provider

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.foreverfreedictionary.data.cloud.WordOfTheDayCloud
import com.example.foreverfreedictionary.data.local.room.WordOfTheDayDao
import com.example.foreverfreedictionary.domain.mapper.WordOfTheDayMapper
import com.example.foreverfreedictionary.vo.Resource
import timber.log.Timber
import java.sql.Date
import javax.inject.Inject
import java.util.*


class WordOfTheDayProvider @Inject constructor(
    private val local: WordOfTheDayDao,
    private val  cloud: WordOfTheDayCloud,
    private val mapper: WordOfTheDayMapper) {

    suspend fun fetchWordOfTheDay(): LiveData<Resource<String>> {
        val now = Calendar.getInstance()
        now.set(Calendar.HOUR, 0)
        now.set(Calendar.MINUTE, 0)
        now.set(Calendar.SECOND, 0)
        now.set(Calendar.MILLISECOND, 0)
        val zeroHourMinutesSecond = now.timeInMillis
        val date = Date(zeroHourMinutesSecond)
        Timber.d("0h-0m-0s timestamp $zeroHourMinutesSecond")
        return Transformations.map(resultLiveData(
            databaseQuery = {
                local.getWordOfTheDay(date)},
            cloudCall = {cloud.fetchWordOfTheDay()},
            saveCloudData = { content ->
                val rowId = local.insertWordOfTheDay(mapper.toData(date, content))
                Timber.d("insert into db at $rowId")
            })){
            mapper.fromData(it)
        }
    }
}