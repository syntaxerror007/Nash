package com.android.nash.data

import com.google.firebase.database.Exclude
import org.parceler.Parcel

@Parcel(Parcel.Serialization.BEAN)
data class TherapistDataModel(var uuid: String = "", var therapistName: String = "", var phoneNumber: String = "", var workSince: NashDate = NashDate(), var job: Int = 0, @get:Exclude var assignmentSet: MutableSet<String> = mutableSetOf(), @get:Exclude var isDeleted: Boolean = false) {
    override fun toString(): String {
        return therapistName
    }
}