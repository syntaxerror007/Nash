package com.android.nash.therapist.assignment

import com.android.nash.data.ServiceDataModel
import com.android.nash.data.TherapistDataModel

interface TherapistAssignmentCallback {
    fun onTherapistAssigned(serviceDataModel: ServiceDataModel?, assignedTherapist: List<TherapistDataModel>)
}