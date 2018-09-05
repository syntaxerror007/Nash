package com.android.nash.service.adapter

import com.android.nash.data.ServiceDataModel
import com.android.nash.data.ServiceGroupDataModel
import com.android.nash.expandablerecyclerview.models.ExpandableListPosition

interface ServiceItemCallback {
    fun onItemEdit(serviceGroupDataModel: ServiceGroupDataModel, serviceDataModel: ServiceDataModel?, groupPosition: Int, childPosition:Int) {}
    fun onItemDelete(serviceGroupDataModel: ServiceGroupDataModel, serviceDataModel: ServiceDataModel?, groupPosition: Int, childPosition:Int) {}
}