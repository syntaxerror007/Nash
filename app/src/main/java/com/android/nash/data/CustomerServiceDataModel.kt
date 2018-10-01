package com.android.nash.data

import com.google.firebase.database.Exclude
import org.parceler.Parcel

@Parcel
data class CustomerServiceDataModel(
        var uuid: String = "",
        var customerUUID: String = "",
        var customerName: String = "",
        var locationUUID: String = "",
        var locationName: String = "",
        var serviceUUID: String = "",
        var serviceName: String = "",
        var therapistUUID: String = "",
        var therapistName: String = "",
        var treatmentDate: NashDate = NashDate(),
        @get:Exclude var service: ServiceDataModel = ServiceDataModel(),
        @get:Exclude var therapist: TherapistDataModel = TherapistDataModel(),
        var price: Long = 0,
        var hasReminded: Boolean = false
)