package com.nash.android.service.form

import com.nash.android.data.ServiceDataModel
import com.nash.android.data.ServiceGroupDataModel

interface ServiceCallback {
    fun onServiceCreated(serviceDataModel: ServiceDataModel, serviceGroupDataModel: ServiceGroupDataModel?, groupPosition: Int, position: Int) {}
    fun onEditService(prevServiceDataModel: ServiceDataModel?, serviceGroupDataModel: ServiceGroupDataModel?, groupPosition: Int, position: Int) {}
}