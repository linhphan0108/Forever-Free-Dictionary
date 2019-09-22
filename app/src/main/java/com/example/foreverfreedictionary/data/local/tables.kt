package com.example.foreverfreedictionary.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.sql.Date

@Entity(
    tableName = "history",
    foreignKeys = [ForeignKey(
        entity = TblDictionary::class,
        parentColumns = arrayOf("query"),
        childColumns = arrayOf("query"),
        onDelete = CASCADE)
        ],
    indices = [Index("word")])
data class TblHistory(
    @PrimaryKey
    @field:SerializedName("query")
    val query: String,
    @field:SerializedName("word")
    val word: String,
    @field:SerializedName("last_access")
    val lastAccess: Date){
    override fun toString(): String {
        return "$word - $lastAccess"
    }
}

@Entity(tableName = "dictionary",
    indices = [Index("word")])
data class TblDictionary(
    @PrimaryKey
    @field:SerializedName("query")
    val query: String,
    @field:SerializedName("word")
    val word: String,
    @field:SerializedName("topic")
    val topic: String?,
    @field:SerializedName("check_spell_page")
    val isCheckSpellPage: Boolean,
    @field:SerializedName("content")
    val content: String,
    @field:SerializedName("sound_br")
    val soundBr: String?,
    @field:SerializedName("sound_ame")
    val soundAme: String?,
    @field:SerializedName("ipa_br")
    val ipaBr: String?,
    @field:SerializedName("ipa_Ame")
    val ipaAme: String?,
    @field:SerializedName("last_access")
    val lastAccess: Date ){

    override fun toString(): String {
        return "$word - $lastAccess"
    }
}

@Entity(tableName = "word-of-the-day")
data class TblWordOfTheDay(
    @PrimaryKey
    @field:SerializedName("date")
    val date: Date,
    @field:SerializedName("content")
    val content: String){

    override fun toString(): String {
        return "$date - $"
    }
}