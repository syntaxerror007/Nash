package com.android.nash.provider

import com.android.nash.data.LocationDataModel
import com.android.nash.data.UserDataModel
import com.android.nash.util.LOCATION_DB
import com.androidhuman.rxfirebase2.database.RxFirebaseDatabase
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Observable
import javax.inject.Inject

class UserProvider {
    private val mFirebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    internal val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val mDatabaseReference = mFirebaseDatabase.getReference(LOCATION_DB)

    fun insertUser(userDataModel: UserDataModel): Task<Void> {
        return mDatabaseReference.child(userDataModel.id).setValue(userDataModel)
    }

    fun getUser(): Observable<List<UserDataModel>> {
        return RxFirebaseDatabase.data(mDatabaseReference).flatMapObservable {
            val locations = it.children.mapNotNull { it.getValue(UserDataModel::class.java) }
            return@flatMapObservable Observable.fromArray(locations)
        }
    }
}