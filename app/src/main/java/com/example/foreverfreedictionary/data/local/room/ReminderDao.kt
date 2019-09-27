package com.example.foreverfreedictionary.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.foreverfreedictionary.data.local.TblReminder
import com.example.foreverfreedictionary.data.local.model.Reminder
import java.sql.Date

@Dao
interface ReminderDao {
    @Query("SELECT query, word, soundBr, soundAme, ipaBr, ipaAme, isReminded, time\n" +
            "FROM reminder\n" +
            "GROUP BY isReminded, query\n" +
            "ORDER BY isReminded ASC, time DESC")
    fun getReminders() : LiveData<List<Reminder>>

    @Query("SELECT * FROM reminder WHERE time <= :time")
    fun getRemindersInTime(time: Long) : LiveData<List<Reminder>>

    @Query("SELECT count(*) FROM reminder WHERE time <= :time")
    fun countRemindersInTime(time: Long) : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReminder(data: TblReminder) : Long

    @Query("UPDATE reminder SET isReminded = 1 WHERE `query` in (:queryList)")
    fun setReminded(queryList: List<String>) : Int

    @Query("UPDATE reminder SET isReminded = :isReminded, time =:time\n WHERE `query` = :query" )
    fun updateReminder(query: String, isReminded: Boolean, time: Date): Int

    @Query("DELETE FROM reminder WHERE `query` = :query")
    fun delete(query: String): Int
}