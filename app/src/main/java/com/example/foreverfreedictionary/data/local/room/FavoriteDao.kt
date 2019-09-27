package com.example.foreverfreedictionary.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foreverfreedictionary.data.local.TblFavorite
import com.example.foreverfreedictionary.data.local.model.FavoriteDictionary

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite")
    fun getFavorite(): LiveData<List<FavoriteDictionary>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favorite: TblFavorite): Long

    @Query("DELETE FROM favorite WHERE `query` = :query")
    fun delete(query: String): Int

}