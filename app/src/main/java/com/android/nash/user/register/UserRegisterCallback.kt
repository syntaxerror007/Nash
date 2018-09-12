package com.android.nash.user.register

import com.android.nash.data.UserDataModel

interface UserRegisterCallback {
    fun onUserCreated(userDataModel: UserDataModel, password: String?)
}