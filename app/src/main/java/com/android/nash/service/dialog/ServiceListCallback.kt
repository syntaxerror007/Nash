package com.android.nash.service.dialog

import com.android.nash.data.ServiceGroupDataModel

interface ServiceListCallback {
    fun onFinishServiceClick(selectedServices: List<ServiceGroupDataModel>)
}