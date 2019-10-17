package com.example.foreverfreedictionary.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foreverfreedictionary.data.local.TblMyVocabulary

@Dao
interface MyVocabularyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: TblMyVocabulary): Long

    @Query("SELECT * FROM my_vocabulary WHERE `query` = :query")
    fun getAllMyVocabularyByQuery(query: String): LiveData<List<TblMyVocabulary>>

    @Query("SELECT * FROM my_vocabulary WHERE groupName = :groupName")
    fun getAllMyVocabulary(groupName: String): LiveData<List<TblMyVocabulary>>

    @Query("DELETE FROM my_vocabulary WHERE `query` = :query AND groupName = :groupName")
    fun delete(query: String, groupName: String): Int
}