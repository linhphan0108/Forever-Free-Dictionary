package com.example.foreverfreedictionary.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foreverfreedictionary.data.local.TblMyVocabularyGroup

@Dao
interface MyVocabularyGroupDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: TblMyVocabularyGroup): Long

    @Query("SELECT * FROM my_vocabulary_group")
    fun getAllMyVocabularyGroup(): LiveData<List<TblMyVocabularyGroup>>

    @Query("DELETE FROM my_vocabulary_group WHERE `name` = :name")
    fun delete(name: String): Int
}