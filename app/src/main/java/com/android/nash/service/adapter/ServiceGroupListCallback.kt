package com.android.nash.service.adapter

import com.android.nash.data.ServiceGroupDataModel

interface ServiceGroupListCallback {
    fun onEditGroup(serviceGroupDataModel: ServiceGroupDataModel?, position: Int) {}
    fun onAddService(serviceGroupDataModel: ServiceGroupDataModel?, position: Int) {}
    fun onDeleteServiceGroup(serviceGroupDataModel: ServiceGroupDataModel?, position: Int) {}
}