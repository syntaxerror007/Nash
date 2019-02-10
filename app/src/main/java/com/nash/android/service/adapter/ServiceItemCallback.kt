package com.nash.android.service.adapter

import com.nash.android.data.ServiceDataModel
import com.nash.android.data.ServiceGroupDataModel

interface ServiceItemCallback {
    fun onItemEdit(serviceGroupDataModel: ServiceGroupDataModel, serviceDataModel: ServiceDataModel?, groupPosition: Int, childPosition: Int) {}
    fun onItemDelete(serviceGroupDataModel: ServiceGroupDataModel, serviceDataModel: ServiceDataModel?, groupPosition: Int, childPosition: Int) {}
}