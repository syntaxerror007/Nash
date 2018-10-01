package com.android.nash.customer.customerservice.customerservicedialog

import com.android.nash.core.CoreViewModel
import com.android.nash.data.ServiceGroupDataModel
import com.android.nash.data.TherapistDataModel

class CustomerAddServiceFormViewModel : CoreViewModel() {

    var serviceGroupDataModel: List<ServiceGroupDataModel>? = null
    var therapistDataModel: List<TherapistDataModel>? = null
    var locationUUID: String? = null


    fun setFormData(formData: CustomerServiceDialogFormData) {
        serviceGroupDataModel = formData.servicesGroups
        therapistDataModel = formData.therapists
        locationUUID = formData.locationUUID
    }

}