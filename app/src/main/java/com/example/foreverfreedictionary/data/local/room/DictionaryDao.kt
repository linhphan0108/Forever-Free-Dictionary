package com.example.foreverfreedictionary.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foreverfreedictionary.data.local.TblDictionary
import com.example.foreverfreedictionary.data.local.model.DictionaryData

@Dao
interface DictionaryDao {
    @Query("SELECT dictionary.query, dictionary.word, dictionary.topic, dictionary.isCheckSpellPage," +
            " dictionary.content, dictionary.soundBr, dictionary.soundAme, dictionary.ipaBr, dictionary.ipaAme," +
            " favorite.isFavorite, reminder.isReminded, reminder.time, dictionary.lastAccess " +
            " FROM dictionary" +
            " LEFT JOIN reminder ON reminder.query = dictionary.query" +
            " LEFT JOIN favorite ON favorite.query = dictionary.query" +
            " WHERE dictionary.query = :query LIMIT 1")
    fun getDictionary(query: String): LiveData<DictionaryData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDictionary(data: TblDictionary) : Long
}