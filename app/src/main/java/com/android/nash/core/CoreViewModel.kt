package com.android.nash.core

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.android.nash.data.UserDataModel
import com.android.nash.util.USER_DB
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

open class CoreViewModel : ViewModel() {
    internal val user: MutableLiveData<FirebaseUser> = MutableLiveData()
    internal val userDataModel: MutableLiveData<UserDataModel> = MutableLiveData();
    internal val mFirebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    internal val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    internal var mDatabaseReference: DatabaseReference
    init {
        val loggedInUser = mAuth.currentUser
        user.value = loggedInUser
        mDatabaseReference = mFirebaseDatabase.getReference(USER_DB)
        if (loggedInUser != null)
            mDatabaseReference.child(loggedInUser.uid).addValueEventListener(object: ValueEventListener {
                override fun onDataChange(data: DataSnapshot) {
                    if (data.exists()) {
                        userDataModel.value = data.getValue(UserDataModel::class.java)
                    }
                }

                override fun onCancelled(data: DatabaseError) {
                    return
                }

            })
    }

    fun getUser():LiveData<FirebaseUser> {
        return user
    }
}