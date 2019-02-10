package com.nash.android.service

import com.nash.android.data.ServiceGroupDataModel

interface ServiceGroupCallback {
    fun onCreateServiceGroup(serviceGroupDataModel: ServiceGroupDataModel?, newServiceGroupName: String)
}