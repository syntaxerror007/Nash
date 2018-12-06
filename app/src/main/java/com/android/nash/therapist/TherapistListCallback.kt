package com.android.nash.therapist

import com.android.nash.data.TherapistDataModel

interface TherapistListCallback {
    fun onTherapistEdit(therapistDataModel: TherapistDataModel, position: Int)
    fun onTherapistDelete(therapistDataModel: TherapistDataModel, position: Int)
}