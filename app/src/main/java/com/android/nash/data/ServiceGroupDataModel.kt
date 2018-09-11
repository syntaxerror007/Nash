package com.android.nash.data

import android.os.Parcelable
import com.android.nash.expandablecheckrecyclerview.models.MultiCheckExpandableGroup
import com.google.firebase.database.Exclude
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ServiceGroupDataModel(var uuid:String = "", var serviceGroupName:String = "", @get:Exclude var services:MutableList<ServiceDataModel> = mutableListOf()) : MultiCheckExpandableGroup(serviceGroupName, services), Parcelable