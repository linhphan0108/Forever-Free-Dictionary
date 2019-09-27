package com.example.foreverfreedictionary.data.local

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.google.gson.annotations.SerializedName
import retrofit2.http.Field
import java.sql.Date

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
    @field:SerializedName("favorite")
    val isFavorite: Boolean,
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

@Entity(
    tableName = "history"
//    foreignKeys = [ForeignKey(
//        entity = TblDictionary::class,
//        parentColumns = arrayOf("query"),
//        childColumns = arrayOf("query"),
//        onDelete = CASCADE)
//        ],
//    indices = [Index("word")]
)
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

@Entity(tableName = "favorite"
//    foreignKeys = [ForeignKey(
//        entity = TblDictionary::class,
//        parentColumns = arrayOf("query"),
//        childColumns = arrayOf("query"),
//        onDelete = CASCADE)
//    ]
)
data class TblFavorite(
    @PrimaryKey
    @field:SerializedName("query")
    val query: String,
    @field:SerializedName("word")
    val word: String,
    @field:SerializedName("sound_br")
    val soundBr: String?,
    @field:SerializedName("sound_ame")
    val soundAme: String?,
    @field:SerializedName("ipa_br")
    val ipaBr: String?,
    @field:SerializedName("ipa_Ame")
    val ipaAme: String?,
    @field:SerializedName("favorite")
    val isFavorite: Boolean
)

@Entity(tableName = "reminder"
//    foreignKeys = [ForeignKey(
//        entity = TblDictionary::class,
//        parentColumns = arrayOf("query"),
//        childColumns = arrayOf("query"),
//        onDelete = CASCADE)
//    ]
)
data class TblReminder(
    @PrimaryKey
    @field:SerializedName("query")
    val query: String,
    @field:SerializedName("word")
    val word: String,
    @field:SerializedName("sound_br")
    val soundBr: String?,
    @field:SerializedName("sound_ame")
    val soundAme: String?,
    @field:SerializedName("ipa_br")
    val ipaBr: String?,
    @field:SerializedName("ipa_Ame")
    val ipaAme: String?,
    @field:SerializedName("reminded")
    val isReminded: Boolean,
    @field:SerializedName("remind")
    @ColumnInfo(name = "time")
    val time: Date
)