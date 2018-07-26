package com.android.nash.service.adapter

import com.android.nash.data.ServiceGroupDataModel

interface ServiceGroupListCallback {
    fun onEditGroup(serviceGroupDataModel: ServiceGroupDataModel?)
    fun onAddService(serviceGroupDataModel: ServiceGroupDataModel?)
}