package com.android.nash.provider

import com.android.nash.data.LocationDataModel
import com.android.nash.data.ServiceGroupDataModel
import com.android.nash.util.SERVICE_GROUP_DB
import com.androidhuman.rxfirebase2.database.RxFirebaseDatabase
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Observable


class ServiceProvider {
    private val mFirebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    internal val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val mDatabaseReference = mFirebaseDatabase.getReference(SERVICE_GROUP_DB)

    fun insertServiceGroup(serviceGroupDataModel: ServiceGroupDataModel, onCompleteListener: OnCompleteListener<Void>) {
        val uuid = mDatabaseReference.push().key
        serviceGroupDataModel.uuid = uuid!!
        mDatabaseReference.child(uuid).setValue(serviceGroupDataModel).addOnCompleteListener(onCompleteListener)
    }

    fun getAllServiceGroup(): Observable<List<ServiceGroupDataModel>> {
        return RxFirebaseDatabase.data(mDatabaseReference).flatMapObservable {
            val locations = it.children.mapNotNull{ it.getValue(ServiceGroupDataModel::class.java)}
            return@flatMapObservable Observable.fromArray(locations)
        }
    }
}
