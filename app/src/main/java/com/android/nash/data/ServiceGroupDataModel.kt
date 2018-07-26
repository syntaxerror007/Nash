package com.android.nash.data

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup

data class ServiceGroupDataModel(var uuid:String = "", var serviceGroupName:String = "", var services:MutableList<ServiceDataModel> = mutableListOf()) : ExpandableGroup<ServiceDataModel>(serviceGroupName, services)