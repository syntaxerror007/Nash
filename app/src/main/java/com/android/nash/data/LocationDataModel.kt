package com.android.nash.data

import com.google.firebase.database.Exclude

data class LocationDataModel(var locationName: String = "", var locationAddress: String = "", var phoneNumber: String = "", var uuid: String = "", @get:Exclude var selectedServices: List<ServiceGroupDataModel> = mutableListOf(), var user: UserDataModel = UserDataModel(), @get:Exclude var therapists: List<TherapistDataModel> = mutableListOf(), var totalServices: Int = 0)