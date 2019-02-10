package com.nash.android.data

import com.nash.android.util.CASHIER_TYPE
import org.parceler.Parcel

@Parcel(value = Parcel.Serialization.BEAN)
data class UserDataModel(var id: String = "", var username: String = "", var userType: String = CASHIER_TYPE, var locationUUID: String = "")