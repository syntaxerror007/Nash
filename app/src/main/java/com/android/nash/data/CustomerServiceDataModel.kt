package com.android.nash.data

import com.google.firebase.database.Exclude
import org.parceler.Parcel

@Parcel(value = Parcel.Serialization.BEAN)
data class CustomerServiceDataModel(
        var uuid: String = "",
        var customerUUID: String = "",
        var locationUUID: String = "",
        var serviceGroupUUID: String = "",
        var serviceUUID: String = "",
        var therapistUUID: String = "",
        var treatmentDate: NashDate = NashDate(),
        val toRemindDate: NashDate = NashDate(),
        val treatmentDateTimestamp: Long = 0,
        val toRemindDateTimestamp: Long = 0,
        @get:Exclude var serviceGroup: ServiceGroupDataModel? = ServiceGroupDataModel(),
        @get:Exclude var service: ServiceDataModel? = ServiceDataModel(),
        @get:Exclude var therapist: TherapistDataModel? = TherapistDataModel(),
        @get:Exclude var customerDataModel: CustomerDataModel? = CustomerDataModel(),
        @get:Exclude var locationName: String = "",
        var price: Long = 0,
        var hasReminded: Boolean = false,
        var lashType: String = ""
)