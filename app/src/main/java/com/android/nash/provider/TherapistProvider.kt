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

    fun insertTherapistListToLocation(locationUUID: String, therapists: List<TherapistDataModel>?) {
        mLocationTherapistReference.child(locationUUID)
        therapists?.forEach {
            insertTherapistToLocation(locationUUID, it)
        }
    }

    private fun insertTherapistToLocation(locationUUID: String, therapistDataModel: TherapistDataModel) {
        val locationRef = mLocationTherapistReference.child(locationUUID)
        val uuid = getKey(mTherapistReference)
        therapistDataModel.uuid = uuid
        mTherapistReference.child(uuid).setValue(therapistDataModel).continueWith {
            locationRef.child(uuid).setValue(true)
        }
    }

    fun getTherapistFromLocation(locationUUID: String): Observable<MutableList<TherapistDataModel>> {
        return RxFirebaseDatabase.data(mLocationTherapistReference.child(locationUUID)).toObservable()
                .flatMap {
                    Observable.fromArray(it.children.map {
                        it.key!!
                    })
                }
                .flatMapIterable { it }
                .flatMap {
                    getTherapist(it).toObservable()
                }
                .toList().toObservable()
    }

    fun getAllTherapist(): Observable<MutableList<TherapistDataModel>> {
        return RxFirebaseDatabase.data(mTherapistReference).flatMapObservable {
            if (it.exists()) {
                return@flatMapObservable Observable.fromArray(it.children.mapNotNull { it.getValue(TherapistDataModel::class.java) }.toMutableList())
            } else {
                return@flatMapObservable Observable.just(mutableListOf<TherapistDataModel>())
            }
        }

    }

    fun getTherapist(therapistUUID: String): Single<TherapistDataModel> {
        return RxFirebaseDatabase.data(mTherapistReference.child(therapistUUID)).map {
            it.getValue(TherapistDataModel::class.java)
        }
    }


    fun updateTherapist(locationUUID: String, therapists: MutableList<TherapistDataModel>?) {
        val disposable = RxFirebaseDatabase.removeValue(mLocationTherapistReference.child(locationUUID)).subscribe {
            therapists?.forEach {
                if (it.uuid.isBlank()) {
                    insertTherapistToLocation(locationUUID, it)
                } else {
                    mLocationTherapistReference.child(locationUUID).child(it.uuid).setValue(true)
                }
            }
        }

    }

    fun removeTherapistFromLocation(uuid: String): Task<Void> = mLocationTherapistReference.child(uuid).removeValue()
}