package com.android.nash.data

data class TherapistDataModel(val uuid: String = "", val therapistName: String = "", val phoneNumber: String = "", val workSince: NashDate = NashDate(), val job: Int = 0)