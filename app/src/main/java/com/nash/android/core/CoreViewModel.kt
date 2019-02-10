package com.nash.android.core

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.androidhuman.rxfirebase2.auth.rxSignOut
import com.androidhuman.rxfirebase2.database.RxFirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jakewharton.rxrelay2.PublishRelay
import com.nash.android.data.UserDataModel
import com.nash.android.util.USER_DB
import io.reactivex.Completable

open class CoreViewModel : ViewModel() {
    internal val user: MutableLiveData<FirebaseUser> = MutableLiveData()
    private val userDataModelLiveData: MutableLiveData<UserDataModel> = MutableLiveData()
    internal val mFirebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    internal var mDatabaseReference: DatabaseReference
    internal val autoCompletePublishSubject = PublishRelay.create<String>()

    init {
        val loggedInUser = mAuth.currentUser
        user.value = loggedInUser
        mDatabaseReference = mFirebaseDatabase.getReference(USER_DB)
        if (loggedInUser != null) {
            val disposable = RxFirebaseDatabase.data(mDatabaseReference.child(loggedInUser.uid)).subscribe({
                if (it.exists()) {
                    val userDataModel = it.getValue(UserDataModel::class.java)
                    userDataModelLiveData.value = userDataModel
                }
            }) {

            }

        }
    }

    fun getUser(): LiveData<FirebaseUser> {
        return user
    }

    fun getUserDataModel(): LiveData<UserDataModel> {
        return userDataModelLiveData
    }

    fun onSearchFormTextChanged(s: String) {
        autoCompletePublishSubject.accept(s.trim())
    }

    fun doLogout(): Completable = mAuth.rxSignOut()
}