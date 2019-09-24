package com.example.foreverfreedictionary.ui.model

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import com.example.foreverfreedictionary.R
import java.sql.Date

class ReminderEntity (val query: String,
                      val word: String,
                      val soundBr: String?,
                      val soundAme: String?,
                      val ipaBr: String?,
                      val ipaAme: String?,
                      val isReminded: Boolean,
                      @ColumnInfo(name = "time") val remindTime: Date) : Entity(), Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        Date(parcel.readLong())
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(query)
        parcel.writeString(word)
        parcel.writeString(soundBr)
        parcel.writeString(soundAme)
        parcel.writeString(ipaBr)
        parcel.writeString(ipaAme)
        parcel.writeByte(if (isReminded) 1 else 0)
        parcel.writeLong(remindTime.time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ReminderEntity> {
        override fun createFromParcel(parcel: Parcel): ReminderEntity {
            return ReminderEntity(parcel)
        }

        override fun newArray(size: Int): Array<ReminderEntity?> {
            return arrayOfNulls(size)
        }
    }


    fun combineIpa(context: Context): String{
        return if (ipaBr != null && ipaAme != null){
            context.getString(R.string.ipa_format, ipaBr, ipaAme)
        } else if(ipaBr != null){
            context.getString(R.string.ipa_br_format, ipaBr)
        }else ""
    }

}