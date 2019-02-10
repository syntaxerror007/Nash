package com.nash.android.data

import com.google.firebase.database.Exclude
import org.parceler.Parcel

@Parcel(value = Parcel.Serialization.BEAN)
data class LocationDataModel(var locationName: String = "", var locationAddress: String = "", var phoneNumber: String = "", var uuid: String = "", @get:Exclude var selectedServices: MutableList<ServiceGroupDataModel> = mutableListOf(), @get:Exclude var user: UserDataModel = UserDataModel(), @get:Exclude var therapists: MutableList<TherapistDataModel> = mutableListOf(), var totalServices: Int = 0, @get:Exclude var therapistAssignmentMap: MutableMap<String, MutableList<String>> = mutableMapOf())