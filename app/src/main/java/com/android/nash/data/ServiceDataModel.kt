package com.android.nash.data

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter.writeString



data class ServiceDataModel(var uuid: String = "", var serviceName: String = "", var price: Long = 0, var reminder: Int = 0, var shouldFreeText: Boolean = false, var numTherapist: Int = 0, @get:Exclude var assignedTherapistSet: MutableList<String> = mutableListOf()) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readInt(),

            parcel.readByte() != 0.toByte(),
            parcel.readInt(),
            parcel.createStringArrayList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        with(parcel) {
            writeString(uuid)
            writeString(serviceName)
            writeLong(price)
            writeInt(reminder)
            writeByte(if (shouldFreeText) 1 else 0)
            writeInt(numTherapist)
            writeStringList(assignedTherapistSet)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField val CREATOR = object : Parcelable.Creator<ServiceDataModel> {
            override fun createFromParcel(parcel: Parcel): ServiceDataModel {
                return ServiceDataModel(parcel)
            }

            override fun newArray(size: Int): Array<ServiceDataModel?> {
                return arrayOfNulls(size)
            }
        }
    }
}