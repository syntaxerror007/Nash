package com.android.nash.data

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ServiceDataModel(var id:String, var serviceName:String, var defaultPrice:Long, var reminder:Int, var shouldFreeText:Boolean) : Parcelable