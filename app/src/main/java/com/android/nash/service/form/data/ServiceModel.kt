package com.android.nash.service.form.data

import android.os.Parcel
import android.os.Parcelable


data class ServiceModel(var id:String = "", var serviceName:String = "", var price:Long = 0, var reminder:Int = 0, var shouldFreeText:Boolean = false) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readInt(),
            parcel.readByte() != 0.toByte()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(serviceName)
        parcel.writeLong(price)
        parcel.writeInt(reminder)
        parcel.writeByte(if (shouldFreeText) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField val CREATOR = object : Parcelable.Creator<ServiceModel> {
            override fun createFromParcel(parcel: Parcel): ServiceModel {
                return ServiceModel(parcel)
            }

            override fun newArray(size: Int): Array<ServiceModel?> {
                return arrayOfNulls(size)
            }
        }
    }
}