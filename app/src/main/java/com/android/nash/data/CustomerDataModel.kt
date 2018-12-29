package com.android.nash.data

import com.android.nash.util.convertToString
import com.android.nash.util.toYesNo
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
        @get:Exclude var knowNashFrom: MutableList<String> = mutableListOf(),
        var knowNashFromInfo: String = "",
        var customerLowerCase: String = "",
        @get:Exclude var services:List<ServiceDataModel> = listOf(),
        @get:Exclude var isNeedToBeReminded: Boolean = false
) {
    override fun toString(): String {
        return "CustomerDataModel [id=$uuid, customerName=$customerName, customerEmail=$customerEmail]"
    }

    fun toCsvRow(): Array<String> {
        return arrayOf(customerName,
                customerEmail,
                customerPhone,
                customerAddress,
                customerDateOfBirth.convertToString(),
                hasExtensions.toYesNo(),
                hasExtensionsInfo,
                hasAllergy.toYesNo(),
                hasAllergyInfo,
                wearContactLens.toYesNo(),
                wearContactLensInfo,
                hadSurgery.toYesNo(),
                hadSurgeryInfo,
                knowNashFrom.joinToString(", "),
                knowNashFromInfo)
    }

    companion object {
        fun getCsvHeader(): Array<String> {
            return arrayOf("Name",
                    "Email",
                    "Phone",
                    "Address",
                    "Date of Birth",
                    "Has Extensions Before",
                    "Extensions Additional Info",
                    "Has Allergy",
                    "Allergy Info",
                    "Wear Contact Lens",
                    "Wear Contact Lens Info",
                    "Had Surgery",
                    "Had Surgery Info",
                    "Know Nash From",
                    "Know Nash From Info")
        }
    }
}