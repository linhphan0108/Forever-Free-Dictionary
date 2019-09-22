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


    @Query("SELECT dictionary.query, dictionary.word, dictionary.topic, dictionary.ipaBr, dictionary.ipaAme, dictionary.lastAccess FROM history\n" +
            "LEFT JOIN dictionary ON dictionary.query = history.query\n" +
            "ORDER BY dictionary.lastAccess DESC")
    fun getDictionaryHistory() : LiveData<List<DictionaryHistory>>

//    @Query("SELECT * FROM sets WHERE themeId = :themeId ORDER BY year DESC")
//    fun getPagedLegoSetsByTheme(themeId: Int): DataSource.Factory<Int, LegoSet>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHistory(data: TblHistory) : Long
}