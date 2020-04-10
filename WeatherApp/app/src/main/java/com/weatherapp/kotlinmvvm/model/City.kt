package com.weatherapp.kotlinmvvm.model

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import com.google.gson.annotations.SerializedName


class City() : Parcelable {
    @SerializedName("id")
    var id: Int = 0
    @SerializedName("name")
    var name: String? = null
    @SerializedName("state")
    var state: String? = null
    @SerializedName("country")
    var country: String? = null
    @SerializedName("coord")
    var coord: Coord? = null

    var isChecked: Boolean = false

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        name = parcel.readString()
        state = parcel.readString()
        country = parcel.readString()
        isChecked = parcel.readByte() != 0.toByte()
    }

    override fun toString(): String {
        return name.toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(state)
        parcel.writeString(country)
        parcel.writeByte(if (isChecked) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<City> {
        override fun createFromParcel(parcel: Parcel): City {
            return City(parcel)
        }

        override fun newArray(size: Int): Array<City?> {
            return arrayOfNulls(size)
        }
    }
}