package com.android.nash.customer.customerservice.customerservicedialog

import com.android.nash.data.ServiceGroupDataModel
import com.android.nash.data.TherapistDataModel
import org.parceler.Parcel

@Parcel
data class CustomerServiceDialogFormData(var servicesGroups: List<ServiceGroupDataModel>? = listOf(), var therapists: List<TherapistDataModel>? = listOf(), var locationUUID: String = "")