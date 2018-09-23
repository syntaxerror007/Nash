package com.android.nash.data

data class CustomerServiceDataModel(val uuid: String = "", val treatmentDate: NashDate = NashDate(), val service: ServiceDataModel, val therapist: TherapistDataModel, val price: Long, val hasReminded: Boolean)