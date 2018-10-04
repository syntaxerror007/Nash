package com.android.nash.provider

import com.android.nash.data.TherapistDataModel
import com.android.nash.util.LOCATION_THERAPIST_DB
import com.android.nash.util.THERAPIST_DB
import com.androidhuman.rxfirebase2.database.RxFirebaseDatabase
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Observable
import io.reactivex.Single

class TherapistProvider {
    val mFirebaseDatabase = FirebaseDatabase.getInstance()
    private val mTherapistReference = mFirebaseDatabase.getReference(THERAPIST_DB)
    private val mLocationTherapistReference = mFirebaseDatabase.getReference(LOCATION_THERAPIST_DB)


    private fun getKey(databaseReference: DatabaseReference): String {
        return databaseReference.push().key!!
    }

    fun insertTherapistListToLocation(locationUUID: String, therapists: List<TherapistDataModel>) {
        mLocationTherapistReference.child(locationUUID)
        therapists.forEach {
            insertTherapistToLocation(locationUUID, it)

        }
    }

    fun insertTherapistToLocation(locationUUID:String, therapistDataModel: TherapistDataModel) {
        mLocationTherapistReference.child(locationUUID)
        val uuid = getKey(mTherapistReference)
        therapistDataModel.uuid = uuid
        mTherapistReference.child(uuid).setValue(therapistDataModel).continueWith {
            mLocationTherapistReference.child(uuid).setValue(true)
        }
    }

    fun getTherapistFromLocation(locationUUID: String): Single<MutableList<TherapistDataModel>> {
        return RxFirebaseDatabase.data(mLocationTherapistReference.child(locationUUID)).toObservable()
                .flatMap { Observable.fromArray(it.children.map { it.key!! }) }
                .flatMapIterable { it }
                .flatMap { getTherapist(it).toObservable() }
                .toList()
    }

    fun getTherapist(therapistUUID: String): Single<TherapistDataModel> {
        return RxFirebaseDatabase.data(mTherapistReference.child(therapistUUID)).map {
            it.getValue(TherapistDataModel::class.java)
        }
    }


    fun updateTherapist(locationUUID: String, therapists: MutableList<TherapistDataModel>) {
        therapists.forEach {
            mLocationTherapistReference.child(locationUUID).child(it.uuid).setValue(true)
        }
    }

    fun removeTherapistFromLocation(uuid: String): Task<Void> = mLocationTherapistReference.child(uuid).removeValue()
}