package com.android.nash.data

data class UserDataModel(var id:String, var username:String, var displayName:String, var phoneNumber:String, var userType:String) {
    constructor(): this("","","","", "")
}