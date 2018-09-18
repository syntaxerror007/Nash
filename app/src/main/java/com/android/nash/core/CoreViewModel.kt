package com.android.nash.core

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.android.nash.data.UserDataModel
import com.android.nash.util.USER_DB
import com.androidhuman.rxfirebase2.database.RxFirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

open class CoreViewModel : ViewModel() {
    internal val user: MutableLiveData<FirebaseUser> = MutableLiveData()
    private val userDataModel: MutableLiveData<UserDataModel> = MutableLiveData()
    internal val mFirebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    internal var mDatabaseReference: DatabaseReference
    init {
        val loggedInUser = mAuth.currentUser
        user.value = loggedInUser
        mDatabaseReference = mFirebaseDatabase.getReference(USER_DB)
        if (loggedInUser != null) {
            val disposable = RxFirebaseDatabase.data(mDatabaseReference.child(loggedInUser.uid)).subscribe({
                if (it.exists()) {
                    userDataModel.value = it.getValue(UserDataModel::class.java)
                }
            }) {

            }

        }
    }

    fun getUser():LiveData<FirebaseUser> {
        return user
    }

    fun getUserDataModel():LiveData<UserDataModel> {
        return userDataModel
    }
}