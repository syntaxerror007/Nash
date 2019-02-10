package com.nash.android.user.register

import com.nash.android.data.UserDataModel

interface UserRegisterCallback {
    fun onUserCreated(userDataModel: UserDataModel, password: String?)
}