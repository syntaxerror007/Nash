package com.nash.android.user.register

import android.content.Context
import android.os.Bundle
import com.nash.android.R
import com.nash.android.core.dialog.CoreDialog
import com.nash.android.data.UserDataModel
import kotlinx.android.synthetic.main.register_activity.*

class UserRegisterDialog(context: Context, userCallback: UserRegisterCallback) : CoreDialog<UserRegisterDialogViewModel>(context) {
    private val userRegisterCallback: UserRegisterCallback = userCallback
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)
        btnRegister.setOnClickListener { registerUser() }
        btnCancel.setOnClickListener { cancel() }
    }

    private fun registerUser() {
        if (verifyUserInput()) {
            userRegisterCallback.onUserCreated(UserDataModel(username = editTextUsername.text.toString()), editTextPassword.text.toString())
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