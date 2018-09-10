package com.android.nash.data

import com.android.nash.expandablecheckrecyclerview.models.MultiCheckExpandableGroup
import com.google.firebase.database.Exclude

data class ServiceGroupDataModel(var uuid:String = "", var serviceGroupName:String = "", @get:Exclude var services:MutableList<ServiceDataModel> = mutableListOf()) : MultiCheckExpandableGroup(serviceGroupName, services)