package com.example.foreverfreedictionary.ui.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import java.sql.Date

class DictionaryEntity(
    val query: String,
    val word: String,
    val topic: String?,
    val isCheckSpellPage: Boolean,
    val content: String,
    val soundBr: String?,
    val soundAme: String?,
    val ipaBr: String?,
    val ipaAme: String?,
    val isFavorite: Boolean,
    val isReminded: Boolean,
    @ColumnInfo(name = "time")
    val remindTime: Date?,
    val lastAccess: Date
): Entity(), Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        readRemindTime(parcel),
        Date(parcel.readLong())
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(query)
        parcel.writeString(word)
        parcel.writeString(topic)
        parcel.writeByte(if (isCheckSpellPage) 1 else 0)
        parcel.writeString(content)
        parcel.writeString(soundBr)
        parcel.writeString(soundAme)
        parcel.writeString(ipaBr)
        parcel.writeString(ipaAme)
        parcel.writeByte(if (isFavorite) 1 else 0)
        parcel.writeByte(if (isReminded) 1 else 0)
        parcel.writeLong(remindTime?.time ?: 0)
        parcel.writeLong(lastAccess.time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DictionaryEntity> {
        override fun createFromParcel(parcel: Parcel): DictionaryEntity {
            return DictionaryEntity(parcel)
        }

        override fun newArray(size: Int): Array<DictionaryEntity?> {
            return arrayOfNulls(size)
        }

        fun readRemindTime(parcel: Parcel): Date?{
            val t = parcel.readLong()
            return if (t > 0){
                Date(t)
            }else{
                null
            }
        }
    }
}