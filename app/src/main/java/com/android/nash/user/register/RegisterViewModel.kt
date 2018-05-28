package com.android.nash.user.register

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.android.nash.data.UserDataModel
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterViewModel: ViewModel() {
    private val user: MutableLiveData<FirebaseUser> = MutableLiveData()
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val mFirebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private lateinit var mDatabaseReference: DatabaseReference
    private val loadingState: MutableLiveData<Boolean> = MutableLiveData()
    private val createdUser: MutableLiveData<UserDataModel> = MutableLiveData()
    private val message:MutableLiveData<String> = MutableLiveData()

    fun init() {
        user.value = mAuth.currentUser
        loadingState.value = false
        createdUser.value = UserDataModel()
        mDatabaseReference = mFirebaseDatabase.getReference("user")
    }

    fun getUser(): LiveData<FirebaseUser> {
        return user
    }

    fun getLoadingState(): LiveData<Boolean> {
        return loadingState
    }

    fun getMessage(): LiveData<String> {
        return message
    }

    fun doRegister(username: String?, password: String?, name: String?, phoneNumber: String?) {
        loadingState.value = true
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(username!! + "@nash.com", password!!).addOnCompleteListener {
            loadingState.value = false
            if (it.isSuccessful) {
                val createdUserUid = it.result.user.uid
                val userDataModel = createdUser.value!!
                userDataModel.username = username
                userDataModel.id = createdUserUid
                userDataModel.displayName = name!!
                userDataModel.phoneNumber = phoneNumber!!
                mDatabaseReference.setValue(userDataModel)
            } else {
                var errorMessage:String?
                if (it.exception!! is FirebaseAuthException) {
                    val exception: FirebaseAuthException = it.exception!! as FirebaseAuthException
                    errorMessage = exception.message
                } else if (it.exception is FirebaseNetworkException) {
                    val exception: FirebaseNetworkException = it.exception!! as FirebaseNetworkException
                    errorMessage = exception.message
                } else {
                    errorMessage = null
                }
                message.value = errorMessage
            }
        }
    }
}