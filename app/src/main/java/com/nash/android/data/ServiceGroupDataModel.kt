package com.nash.android.data

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.nash.android.expandablecheckrecyclerview.models.MultiCheckExpandableGroup
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ServiceGroupDataModel(var uuid: String = "", var serviceGroupName: String = "", @get:Exclude var services: MutableList<ServiceDataModel> = mutableListOf()) : MultiCheckExpandableGroup(serviceGroupName, services), Parcelable {
    @get:Exclude
    lateinit var serviceKeys: List<String>

    override fun toString(): String {
        return serviceGroupName
    }
}