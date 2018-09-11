package com.android.nash.data

import org.parceler.Parcel

@Parcel(Parcel.Serialization.BEAN)
data class TherapistDataModel(var uuid: String = "", var therapistName: String = "", var phoneNumber: String = "", var workSince: NashDate = NashDate(), var job: Int = 0)