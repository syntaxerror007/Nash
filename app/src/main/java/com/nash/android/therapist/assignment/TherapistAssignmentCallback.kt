package com.nash.android.therapist.assignment

import com.nash.android.data.ServiceDataModel
import com.nash.android.data.TherapistDataModel

interface TherapistAssignmentCallback {
    fun onTherapistAssigned(serviceDataModel: ServiceDataModel?, assignedTherapist: List<TherapistDataModel>)
}