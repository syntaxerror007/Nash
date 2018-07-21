package com.android.nash.data

data class LocationDataModel(val locationName:String = "", val locationAddress:String = "", var phoneNumber:String = "", var uuid:String = "", var selectedServices:List<ServiceGroupDataModel> = mutableListOf())