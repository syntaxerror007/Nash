package com.android.nash.service.adapter

import com.android.nash.data.ServiceDataModel

interface ServiceItemCallback {
    fun onItemEdit(serviceDataModel: ServiceDataModel?)
    fun onItemDelete(serviceDataModel: ServiceDataModel?)
}