package com.android.nash.service.form

import com.android.nash.data.ServiceDataModel
import com.android.nash.data.ServiceGroupDataModel

interface ServiceCallback {
    fun onServiceCreated(serviceDataModel: ServiceDataModel, serviceGroupDataModel: ServiceGroupDataModel?, groupPosition: Int, position:Int) {}
    fun onEditService(prevServiceDataModel: ServiceDataModel?, serviceGroupDataModel: ServiceGroupDataModel?, groupPosition: Int, position: Int) {}
}