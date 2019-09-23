package com.example.foreverfreedictionary.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foreverfreedictionary.data.local.TblDictionary

@Dao
interface DictionaryDao {
    @Query("SELECT * FROM dictionary WHERE `query` = :query LIMIT 1")
    fun getDictionary(query: String): LiveData<TblDictionary>

    @Query("SELECT * FROM dictionary WHERE isFavorite = 1")
    fun getFavoriteDictionary(): LiveData<List<TblDictionary>>


    @Query("UPDATE dictionary SET isFavorite = :isFavorite WHERE `query` = :query")
    fun insertFavoriteDictionary(query: String, isFavorite: Boolean): Int



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDictionary(data: TblDictionary) : Long
}