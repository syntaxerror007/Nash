package com.android.nash.user.register

import android.content.Context
import android.os.Bundle
import com.android.nash.R
import com.android.nash.core.dialog.CoreDialog
import com.android.nash.data.UserDataModel
import kotlinx.android.synthetic.main.register_activity.*

class UserRegisterDialog(context: Context, userCallback: UserRegisterCallback) : CoreDialog<UserRegisterDialogViewModel>(context) {
    private val userRegisterCallback: UserRegisterCallback = userCallback
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)
        btnRegister.setOnClickListener { registerUser() }
    }

    private fun registerUser() {
        if (verifyUserInput()) {
            userRegisterCallback.onUserCreated(UserDataModel(username = editTextUsername.text.toString(), locationUUID = "-LMgdupDuqrLEdAXvBoY"), editTextPassword.text.toString())
            dismiss()
        }
    }

    private fun verifyUserInput(): Boolean {
        if (editTextUsername.text.isNullOrBlank()) {
            editTextUsername.error = "Please input username"
            return false
        }
        if (editTextPassword.text.isNullOrBlank()) {
            editTextPassword.error = "Please input password"
            return false
        }
        if (editTextRetypePassword.text.isNullOrBlank()) {
            editTextRetypePassword.error = "Please retype password"
            return false
        }
        if (editTextRetypePassword.text.toString() != editTextPassword.text.toString()) {
            editTextRetypePassword.error = "Please input same password"
            return false
        }
        return true
    }

}