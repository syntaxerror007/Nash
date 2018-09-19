package com.android.nash.therapist.register

import com.android.nash.data.TherapistDataModel

interface TherapistRegisterCallback {
    fun onTherapistRegister(therapistDataModel: TherapistDataModel)
}