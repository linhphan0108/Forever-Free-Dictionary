package com.example.foreverfreedictionary.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foreverfreedictionary.data.local.TblHistory
import com.example.foreverfreedictionary.data.local.model.DictionaryHistory

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history ORDER BY lastAccess DESC")
    fun getHistory(): LiveData<List<TblHistory>>

    @Query("SELECT * FROM history ORDER BY lastAccess DESC LIMIT :limited")
    fun getHistory(limited: Int): LiveData<List<TblHistory>>


    @Query("SELECT history.query, history.word, history.topic, history.isCheckSpellPage," +
            " history.soundBr, history.soundAme, history.ipaBr, history.ipaAme, " +
            "favorite.isFavorite, reminder.isReminded, reminder.time, history.lastAccess FROM history\n" +
            "LEFT JOIN favorite ON favorite.query = history.query\n" +
            "LEFT JOIN reminder ON reminder.query = history.query\n" +
            "WHERE history.isCheckSpellPage = 0\n" +
            "ORDER BY history.lastAccess DESC")
    fun getDictionaryHistory() : LiveData<List<DictionaryHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHistory(data: TblHistory) : Long
}