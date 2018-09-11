package com.android.nash.service

import com.android.nash.data.ServiceGroupDataModel

interface ServiceGroupCallback {
    fun onCreateServiceGroup(serviceGroupDataModel: ServiceGroupDataModel?, newServiceGroupName: String)
}