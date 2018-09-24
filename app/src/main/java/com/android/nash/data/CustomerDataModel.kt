package com.android.nash.data

import org.parceler.Parcel

@Parcel
data class CustomerDataModel(val uuid: String = "", val customerName: String = "", val customerEmail: String = "", val customerPhone: String = "", val services:List<ServiceDataModel> = listOf(), val isNeedToBeReminded: Boolean = false)