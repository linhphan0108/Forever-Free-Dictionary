package com.example.foreverfreedictionary.ui.model

import android.os.Parcel
import android.os.Parcelable

class AutoCompletionEntity(val value: String) : Entity(), Parcelable {

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(value)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AutoCompletionEntity> {
        override fun createFromParcel(parcel: Parcel): AutoCompletionEntity {
            val value = parcel.readString() ?: ""
            return AutoCompletionEntity(value)
        }

        override fun newArray(size: Int): Array<AutoCompletionEntity?> {
            return arrayOfNulls(size)
        }
    }
}