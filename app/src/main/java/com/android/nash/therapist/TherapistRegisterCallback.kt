package com.android.nash.therapist

import com.android.nash.data.TherapistDataModel

interface TherapistRegisterCallback {
    fun onTherapistRegister(therapistDataModel: TherapistDataModel)
}