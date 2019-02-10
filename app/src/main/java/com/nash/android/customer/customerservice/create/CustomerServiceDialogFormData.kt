package com.nash.android.customer.customerservice.create

import com.nash.android.data.ServiceGroupDataModel
import com.nash.android.data.TherapistDataModel
import org.parceler.Parcel

@Parcel(value = Parcel.Serialization.BEAN)
data class CustomerServiceDialogFormData(
        var servicesGroups: List<ServiceGroupDataModel>? = listOf(),
        var therapists: List<TherapistDataModel>? = listOf(),
        var locationUUID: String = "",
        var customerUUID: String? = "",
        var customerName: String = "")