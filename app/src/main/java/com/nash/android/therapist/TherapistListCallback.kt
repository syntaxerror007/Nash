package com.nash.android.therapist

import com.nash.android.data.TherapistDataModel

interface TherapistListCallback {
    fun onTherapistEdit(therapistDataModel: TherapistDataModel, position: Int)
    fun onTherapistDelete(therapistDataModel: TherapistDataModel, position: Int)
}