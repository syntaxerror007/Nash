package com.android.nash.data

import com.android.nash.util.CASHIER_TYPE
import org.parceler.Parcel

@Parcel
data class UserDataModel(var id: String = "", var username: String = "", var userType: String = CASHIER_TYPE, var locationUUID: String = "")