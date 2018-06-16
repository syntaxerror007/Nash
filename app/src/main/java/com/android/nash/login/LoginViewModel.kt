package com.android.nash.login

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.android.nash.core.CoreViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : CoreViewModel() {
    private val usernameError: MutableLiveData<String> = MutableLiveData()
    private val passwordError: MutableLiveData<String> = MutableLiveData()
    private val loadingState: MutableLiveData<Boolean> = MutableLiveData()
    private val successLogin: MutableLiveData<Boolean> = MutableLiveData()

    fun getUsernameError():LiveData<String> {
        return usernameError
    }

    fun getPasswordError():LiveData<String> {
        return passwordError
    }

    fun getLoadingState():LiveData<Boolean> {
        return loadingState;
    }

    fun getSuccessLogin():LiveData<Boolean> {
        return successLogin
    }

    fun doLogin(username: String, password: String) {
        loadingState.value = true
        if (!isDataValid(username, password)) {
            return
        }
        FirebaseAuth.getInstance().signInWithEmailAndPassword("$username@nash.com", password).addOnCompleteListener {
            loadingState.value = false
            successLogin.value = it.isSuccessful
        }
    }

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
