package com.android.nash.provider

import com.android.nash.data.UserDataModel
import com.android.nash.util.USER_DB
import com.androidhuman.rxfirebase2.database.RxFirebaseDatabase
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Completable
import io.reactivex.Observable

class UserProvider {
    private val mFirebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    internal val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val mDatabaseReference = mFirebaseDatabase.getReference(USER_DB)

    fun insertUser(userDataModel: UserDataModel): Task<Void> {
        return mDatabaseReference.child(userDataModel.id).setValue(userDataModel)
    }

    fun getUser(): Observable<List<UserDataModel>> {
        return RxFirebaseDatabase.data(mDatabaseReference).flatMapObservable {
            val locations = it.children.mapNotNull { it.getValue(UserDataModel::class.java) }
            return@flatMapObservable Observable.fromArray(locations)
        }
    }

    fun getUserFromUUID(it: String): Observable<UserDataModel> {
        return RxFirebaseDatabase.data(mDatabaseReference.child(it)).flatMapObservable {
            return@flatMapObservable Observable.just(it.getValue(UserDataModel::class.java))
        }
    }

    fun updateUserLocation(userUUID: String, locationUUID: String): Completable {
        return RxFirebaseDatabase.setValue(mDatabaseReference.child(userUUID).child("locationUUID"), locationUUID)
    }
}