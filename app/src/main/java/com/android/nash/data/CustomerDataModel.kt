package com.android.nash.data

import com.google.firebase.database.Exclude
import org.parceler.Parcel

@Parcel(value = Parcel.Serialization.BEAN)
data class CustomerDataModel(
        var uuid: String = "",
        var customerName: String = "",
        var customerEmail: String = "",
        var customerPhone: String = "",
        var customerAddress: String = "",
        var customerDateOfBirth: NashDate = NashDate(),
        var hasExtensions: Boolean = false,
        var hasExtensionsInfo: String = "",
        var hasAllergy: Boolean = false,
        var hasAllergyInfo: String = "",
        var wearContactLens: Boolean = false,
        var wearContactLensInfo: String = "",
        var hadSurgery: Boolean = false,
        var hadSurgeryInfo: String = "",
        var knowNashFrom: String = "",
        var knowNashFromInfo: String = "",
        var customerLowerCase: String = "",
        @get:Exclude var services:List<ServiceDataModel> = listOf(),
        @get:Exclude var isNeedToBeReminded: Boolean = false
)