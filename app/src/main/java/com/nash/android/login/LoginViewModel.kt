package com.nash.android.login

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.nash.android.core.CoreViewModel
import com.nash.android.util.isValidEmail

class LoginViewModel : CoreViewModel() {
    private val usernameError: MutableLiveData<String> = MutableLiveData()
    private val passwordError: MutableLiveData<String> = MutableLiveData()
    private val loadingState: MutableLiveData<Boolean> = MutableLiveData()
    private val successLogin: MutableLiveData<Boolean> = MutableLiveData()

    fun getUsernameError(): LiveData<String> {
        return usernameError
    }

    fun getPasswordError(): LiveData<String> {
        return passwordError
    }

    fun getLoadingState(): LiveData<Boolean> {
        return loadingState;
    }

    fun getSuccessLogin(): LiveData<Boolean> {
        return successLogin
    }

    fun doLogin(username: String, password: String) {
        loadingState.value = true
        if (!isDataValid(username, password)) {
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(getUsername(username), password).addOnCompleteListener {
            loadingState.value = false
            successLogin.value = it.isSuccessful
        }
    }

    private fun getUsername(username: String) = if (username.isValidEmail()) username else "$username@nash.com"

    private fun isDataValid(username: String, password: String): Boolean {
        if (username.isBlank()) {
            usernameError.value = "Mohon masukkan username"
            return false
        }
        if (password.isBlank()) {
            passwordError.value = "Mohon masukkan password"
            return false
        }
        return true
    }
}
