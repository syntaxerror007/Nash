package com.nash.android.service.form.data

data class ServiceGroupModel(var uuid: String = "", var serviceGroupName: String = "", var services: MutableList<ServiceModel> = mutableListOf())