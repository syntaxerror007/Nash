package com.android.nash.user.register

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.android.nash.core.CoreViewModel
import com.android.nash.data.UserDataModel
import com.android.nash.provider.UserProvider
import com.android.nash.util.COMPANY_NAME
import com.android.nash.util.USER_DB
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterViewModel: CoreViewModel() {
    private var userType:String? = null
    private val loadingState: MutableLiveData<Boolean> = MutableLiveData()
    private val message:MutableLiveData<String> = MutableLiveData()
    private val usernameError: MutableLiveData<String> = MutableLiveData()
    private val passwordError: MutableLiveData<String> = MutableLiveData()
    private val nameError: MutableLiveData<String> = MutableLiveData()
    private val phoneError: MutableLiveData<String> = MutableLiveData()
    private val userTypeError: MutableLiveData<String> = MutableLiveData()
    private val userProvider = UserProvider()

    fun init() {
        loadingState.value = false
        mDatabaseReference = mFirebaseDatabase.getReference(USER_DB)
    }

    fun getLoadingState(): LiveData<Boolean> {
        return loadingState
    }

    fun getMessage(): LiveData<String> {
        return message
    }

    fun getUsernameError(): LiveData<String> {
        return usernameError
    }

    fun getPasswordError(): LiveData<String> {
        return passwordError
    }

    fun getPhoneError(): LiveData<String> {
        return phoneError
    }

    fun getNameError(): LiveData<String> {
        return nameError
    }

    fun getUserTypeError(): LiveData<String> {
        return userTypeError
    }

    fun doRegister(secondaryFirebaseApp: FirebaseApp, username: String?, password: String?, name: String?, phoneNumber: String?) {
        if (isFormValid(username, password, name, phoneNumber, userType)) {
            return
        }
        loadingState.value = true
        username!!
        password!!
        val secondaryFirebaseAuth = FirebaseAuth.getInstance(secondaryFirebaseApp)

        secondaryFirebaseAuth.createUserWithEmailAndPassword("$username@$COMPANY_NAME.com", password).addOnCompleteListener {
            if (it.isSuccessful) {
                val createdUserUid = it.result.user.uid
                val userDataModel = UserDataModel()
                userDataModel.username = username
                userDataModel.id = createdUserUid
                userDataModel.displayName = name!!
                userDataModel.phoneNumber = phoneNumber!!
                userDataModel.userType = userType!!

                userProvider.insertUser(userDataModel).addOnCompleteListener {
                    secondaryFirebaseAuth.signOut()
                    loadingState.value = false
                }
            } else {
                loadingState.value = false
                var errorMessage:String? = null
                if (it.exception != null) {
                    errorMessage = when {
                        it.exception is FirebaseAuthException -> {
                            (it.exception as FirebaseAuthException).message
                        }
                        it.exception is FirebaseNetworkException -> {
                            (it.exception as FirebaseNetworkException).message
                        }
                        else -> null
                    }
                }
                message.value = errorMessage
            }
        }
    }

    private fun isFormValid(username: String?, password: String?, name: String?, phoneNumber: String?, userType:String?): Boolean {
        if (username.isNullOrBlank()) {
            usernameError.value = "Mohon masukkan username"
            return true
        }
        usernameError.value = null

        if (password.isNullOrBlank()) {
            passwordError.value = "Mohon masukkan password"
            return true
        }
        passwordError.value = null

        if (name.isNullOrBlank()) {
            nameError.value = "Mohon masukkan nama pengguna"
            return true
        }
        nameError.value = null

        if (phoneNumber.isNullOrBlank()) {
            phoneError.value = "Mohon masukkan nomer Handphone"
            return true
        }
        phoneError.value = null

        if (userType.isNullOrBlank()) {
            userTypeError.value = "Mohon pilih tipe pengguna"
            return true
        }
        userTypeError.value = null

        return false
    }

    fun setItemSelected(item: String) {
        userType = item
        userTypeError.value = null
    }
}