package com.android.nash.login

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.android.nash.data.UserDataModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    private val user: MutableLiveData<FirebaseUser> = MutableLiveData()
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun init() {
        user.value = mAuth.currentUser
    }

    fun getUser(): LiveData<FirebaseUser> {
        return user
    }
}
