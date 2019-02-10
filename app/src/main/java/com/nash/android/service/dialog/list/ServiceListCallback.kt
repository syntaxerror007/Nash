package com.nash.android.service.dialog.list

import com.nash.android.data.ServiceGroupDataModel

interface ServiceListCallback {
    fun onFinishServiceClick(selectedServices: MutableList<ServiceGroupDataModel>)
}