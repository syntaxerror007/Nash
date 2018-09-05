package com.android.nash.service.dialog.list

import com.android.nash.data.ServiceGroupDataModel

interface ServiceListCallback {
    fun onFinishServiceClick(selectedServices: MutableList<ServiceGroupDataModel>)
}