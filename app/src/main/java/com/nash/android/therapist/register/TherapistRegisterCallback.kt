package com.nash.android.therapist.register

import com.nash.android.data.TherapistDataModel

interface TherapistRegisterCallback {
    fun onTherapistRegister(therapistDataModel: TherapistDataModel, position: Int)
}