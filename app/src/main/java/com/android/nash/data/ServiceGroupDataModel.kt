package com.android.nash.data

import com.android.nash.expandablecheckrecyclerview.models.MultiCheckExpandableGroup
import com.android.nash.expandablerecyclerview.models.ExpandableGroup

data class ServiceGroupDataModel(var uuid:String = "", var serviceGroupName:String = "", var services:MutableList<ServiceDataModel> = mutableListOf()) : MultiCheckExpandableGroup(serviceGroupName, services)