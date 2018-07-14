package com.android.nash.provider

import com.android.nash.data.LocationDataModel
import com.android.nash.data.UserDataModel
import com.android.nash.util.LOCATION_DB
import com.androidhuman.rxfirebase2.database.RxFirebaseDatabase
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Observable
class LocationProvider {
    private val mFirebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    internal val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val mDatabaseReference = mFirebaseDatabase.getReference(LOCATION_DB)

    fun insertLocation(locationDataModel: LocationDataModel, onCompleteListener: OnCompleteListener<Void>) {
        val uuid = mDatabaseReference.push().key
        locationDataModel.uuid = uuid!!
        mDatabaseReference.push().child(uuid).setValue(locationDataModel).addOnCompleteListener(onCompleteListener)
    }

    fun getAllLocation(): Observable<List<LocationDataModel>> {
        val mDatabaseReference = mFirebaseDatabase.getReference(LOCATION_DB)
        return RxFirebaseDatabase.data(mDatabaseReference).flatMapObservable {
            val locations = it.children.mapNotNull{ it.getValue(LocationDataModel::class.java)}
            return@flatMapObservable Observable.fromArray(locations)
        }
    }
}
