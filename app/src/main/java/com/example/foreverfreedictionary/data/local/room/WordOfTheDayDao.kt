package com.example.foreverfreedictionary.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foreverfreedictionary.data.local.TblWordOfTheDay
import java.sql.Date

@Dao
interface WordOfTheDayDao {
    @Query("SELECT * FROM `word-of-the-day` WHERE date = :date LIMIT 1")
    fun getWordOfTheDay(date: Date): LiveData<TblWordOfTheDay>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWordOfTheDay(data: TblWordOfTheDay): Long
}