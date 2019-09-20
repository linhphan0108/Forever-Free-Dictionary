package com.example.foreverfreedictionary.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foreverfreedictionary.data.local.TblDictionary

@Dao
interface DictionaryDao {
    @Query("SELECT * FROM dictionary WHERE word = :word LIMIT 1")
    fun getDictionary(word: String): LiveData<TblDictionary>

//    @Query("SELECT * FROM sets WHERE themeId = :themeId ORDER BY year DESC")
//    fun getPagedLegoSetsByTheme(themeId: Int): DataSource.Factory<Int, LegoSet>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDictionary(data: TblDictionary) : Long
}