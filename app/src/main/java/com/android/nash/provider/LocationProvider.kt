package com.android.nash.provider

import com.android.nash.data.*
import com.android.nash.util.LOCATION_DB
import com.androidhuman.rxfirebase2.database.RxFirebaseDatabase
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Observable
import com.google.firebase.database.GenericTypeIndicator




class LocationProvider {
    private val mFirebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val mDatabaseReference = mFirebaseDatabase.getReference(LOCATION_DB)

    fun insertLocation(locationDataModel: LocationDataModel, selectedServiceGroup: List<ServiceGroupDataModel>, therapists: MutableList<TherapistDataModel>, onCompleteListener: OnCompleteListener<Unit>) {
        val uuid = mDatabaseReference.push().key
        locationDataModel.uuid = uuid!!
        val locationRef = mDatabaseReference.child(uuid)
        locationRef.setValue(locationDataModel).continueWith {
            if (it.isSuccessful) {
                val selectedServiceGroupRef = locationRef.child("selectedServices")
                insertSelectedServiceGroup(selectedServiceGroupRef, selectedServiceGroup)

                val therapistsRef = locationRef.child("therapists")
                insertTherapists(therapistsRef, therapists)
            }
        }.addOnCompleteListener(onCompleteListener)

    }

    private fun insertTherapists(therapistsRef: DatabaseReference, therapists: MutableList<TherapistDataModel>) {
        therapists.forEach {
            therapistsRef.child(it.therapistName).setValue(it)
        }
    }

    private fun insertSelectedServiceGroup(selectedServiceGroupRef: DatabaseReference, selectedServiceGroup: List<ServiceGroupDataModel>) {
        selectedServiceGroup.forEach { serviceGroup ->
            selectedServiceGroupRef.child(serviceGroup.serviceGroupName).setValue(serviceGroup).addOnCompleteListener {
                if (it.isSuccessful) {
                    serviceGroup.services.forEach {serviceDataModel ->
                        selectedServiceGroupRef.child(serviceGroup.serviceGroupName).child("services").child(serviceDataModel.serviceName).setValue(serviceDataModel)
                    }
                }
            }
        }
    }

    fun getAllLocation(): Observable<List<LocationDataModel>> {
        return RxFirebaseDatabase.data(mDatabaseReference).flatMapObservable {
            val locations = it.children.mapNotNull{
                val locationDataModel = it.getValue(LocationDataModel::class.java)
                locationDataModel?.therapists = it.child("therapists").children.mapNotNull { it.getValue(TherapistDataModel::class.java) }
                locationDataModel?.selectedServices = it.child("selectedServices").children.mapNotNull {
                    val serviceGroup = it.getValue(ServiceGroupDataModel::class.java)
                    serviceGroup?.services = it.child("services").children.mapNotNull { it.getValue(ServiceDataModel::class.java) }.toMutableList()
                    return@mapNotNull serviceGroup
                }
                return@mapNotNull locationDataModel
            }
            return@flatMapObservable Observable.fromArray(locations)
        }
    }
}
