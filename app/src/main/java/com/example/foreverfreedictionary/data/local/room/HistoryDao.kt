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


    @Query("SELECT dictionary.query, dictionary.word, dictionary.topic, dictionary.isCheckSpellPage," +
            " dictionary.ipaBr, dictionary.ipaAme, dictionary.isFavorite, dictionary.lastAccess FROM history\n" +
            "            LEFT JOIN dictionary ON dictionary.query = history.query\n" +
            "\t\t\tWHERE dictionary.isCheckSpellPage = 0\n" +
            "            ORDER BY dictionary.lastAccess DESC")
    fun getDictionaryHistory() : LiveData<List<DictionaryHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHistory(data: TblHistory) : Long
}