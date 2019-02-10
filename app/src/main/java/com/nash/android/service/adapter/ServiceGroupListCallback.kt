package com.nash.android.service.adapter

import com.nash.android.data.ServiceGroupDataModel

interface ServiceGroupListCallback {
    fun onEditGroup(serviceGroupDataModel: ServiceGroupDataModel?, position: Int) {}
    fun onAddService(serviceGroupDataModel: ServiceGroupDataModel?, position: Int) {}
    fun onDeleteServiceGroup(serviceGroupDataModel: ServiceGroupDataModel?, position: Int) {}
}