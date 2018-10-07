package com.android.nash.provider

import com.android.nash.data.*
import com.android.nash.util.LOCATION_DB
import com.android.nash.util.LOCATION_SERVICE_GROUP_DB
import com.android.nash.util.LOCATION_USER_DB
import com.androidhuman.rxfirebase2.database.RxFirebaseDatabase
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction


class LocationProvider {
    private val mFirebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val mDatabaseReference = mFirebaseDatabase.getReference(LOCATION_DB)
    private val mLocationServiceGroupReference = mFirebaseDatabase.getReference(LOCATION_SERVICE_GROUP_DB)
    private val mLocationUserReference = mFirebaseDatabase.getReference(LOCATION_USER_DB)
    private val mTherapistProvider = TherapistProvider()
    private val mUserProvider = UserProvider()


    fun getKey(databaseReference: DatabaseReference): String {
        return databaseReference.push().key!!
    }

    fun insertLocation(locationDataModel: LocationDataModel, selectedServiceGroup: List<ServiceGroupDataModel>, therapists: MutableList<TherapistDataModel>, assignmentTherapistMap: Map<String, List<TherapistDataModel>>, onCompleteListener: OnCompleteListener<Task<Void>>) {
        val locationUUID = getKey(mDatabaseReference)
        locationDataModel.uuid = locationUUID
        val locationRef = mDatabaseReference.child(locationUUID)
        locationRef.setValue(locationDataModel).continueWith {
            if (it.isSuccessful) {
                mTherapistProvider.insertTherapistListToLocation(locationUUID, therapists)
            }
        }.continueWith {
            if (it.isSuccessful) {
                val selectedServiceGroupRef = mLocationServiceGroupReference.child(locationUUID)
                insertSelectedServiceGroup(selectedServiceGroupRef, selectedServiceGroup, assignmentTherapistMap)
            }
        }.continueWith {
            mUserProvider.updateUserLocation(locationDataModel.user.id, locationUUID).subscribe()
            mLocationUserReference.child(locationUUID).child(locationDataModel.user.id).setValue(true)
        }.addOnCompleteListener(onCompleteListener)
    }

    fun updateLocation(locationDataModel: LocationDataModel, selectedServiceGroup: List<ServiceGroupDataModel>, therapists: MutableList<TherapistDataModel>, assignmentTherapistMap: Map<String, List<TherapistDataModel>>, onCompleteListener: OnCompleteListener<Task<Void>>) {
        val locationUUID = getKey(mDatabaseReference)
        locationDataModel.uuid = locationUUID
        val locationRef = mDatabaseReference.child(locationUUID)
        locationRef.setValue(locationDataModel).continueWith {
            if (it.isSuccessful) {
                val selectedServiceGroupRef = mLocationServiceGroupReference.child(locationUUID)
                insertSelectedServiceGroup(selectedServiceGroupRef, selectedServiceGroup, assignmentTherapistMap)
            }
        }.continueWith {
            if (it.isSuccessful) {
                mTherapistProvider.updateTherapist(locationUUID, therapists)
            }
        }.continueWith {
            mLocationUserReference.child(locationUUID).child(locationDataModel.user.id).setValue(true)
        }.addOnCompleteListener(onCompleteListener)
    }

    private fun insertSelectedServiceGroup(selectedServiceGroupRef: DatabaseReference, selectedServiceGroup: List<ServiceGroupDataModel>, assignmentTherapistMap: Map<String, List<TherapistDataModel>>) {
        selectedServiceGroup.forEach { serviceGroup ->
            serviceGroup.services.map { serviceDataModel ->
                assignmentTherapistMap[serviceDataModel.uuid]?.map {
                    selectedServiceGroupRef.child("service_groups").child(serviceGroup.uuid).child(serviceDataModel.uuid).child(it.uuid).setValue(true)
                }
            }
        }
    }

    fun getAllLocation(): Observable<List<LocationDataModel>> {
        return RxFirebaseDatabase.data(mDatabaseReference).flatMapObservable { Observable.fromArray(it.children.mapNotNull { return@mapNotNull it.getValue(LocationDataModel::class.java) }) }
                .flatMapIterable { it }
                .flatMap { location: LocationDataModel ->
                    Observable.zip(Observable.just(location), mTherapistProvider.getTherapistFromLocation(location.uuid).toObservable(), BiFunction { location: LocationDataModel, therapists: MutableList<TherapistDataModel> ->
                        location.therapists = therapists
                        Observable.just(location)
                    })
                }.flatMap { it }
                .flatMap { location: LocationDataModel ->
                    Observable.zip(Observable.just(location), getServiceGroups(location.uuid), BiFunction { location: LocationDataModel, serviceGroups: MutableList<ServiceGroupDataModel> ->
                        location.selectedServices = serviceGroups
                        Observable.just(location)
                    })
                }.flatMap { it }
//                .flatMap { location: LocationDataModel ->
//                    Observable.zip(Observable.just(location), getTherapistAssignment(location), BiFunction { locationDataModel: LocationDataModel, therapistAssignmentMap: MutableMap<String, MutableList<String>> ->
//                        locationDataModel.therapistAssignmentMap = therapistAssignmentMap
//                        Observable.just(locationDataModel)
//                    })
//                }
//                .flatMap { it }
                .flatMap { location: LocationDataModel ->
                    Observable.zip(Observable.just(location), getUser(location.uuid), BiFunction { location: LocationDataModel, userDataModel: UserDataModel ->
                        location.user = userDataModel
                        Observable.just(location)
                    })
                }.flatMap { it }
                .toList().toObservable()
    }

    fun getLocationFromUUID(locationUUID: String): Observable<LocationDataModel> {
        return RxFirebaseDatabase.data(mDatabaseReference.child(locationUUID)).flatMapObservable { Observable.just(it.getValue(LocationDataModel::class.java)) }
                .flatMap { location: LocationDataModel ->
                    Observable.zip(Observable.just(location), mTherapistProvider.getTherapistFromLocation(location.uuid).toObservable(), BiFunction { location: LocationDataModel, therapists: MutableList<TherapistDataModel> ->
                        location.therapists = therapists
                        Observable.just(location)
                    })
                }.flatMap { it }
                .flatMap { location: LocationDataModel ->
                    Observable.zip(Observable.just(location), getServiceGroups(location.uuid), BiFunction { location: LocationDataModel, serviceGroups: MutableList<ServiceGroupDataModel> ->
                        location.selectedServices = serviceGroups
                        Observable.just(location)
                    })
                }.flatMap { it }
//                .flatMap { location: LocationDataModel ->
//                    Observable.zip(Observable.just(location), getTherapistAssignment(location), BiFunction { locationDataModel: LocationDataModel, therapistAssignmentMap: MutableMap<String, MutableList<String>> ->
//                        locationDataModel.therapistAssignmentMap = therapistAssignmentMap
//                        Observable.just(locationDataModel)
//                    })
//                }
//                .flatMap { it }
                .flatMap { location: LocationDataModel ->
                    Observable.zip(Observable.just(location), getUser(location.uuid), BiFunction { location: LocationDataModel, userDataModel: UserDataModel ->
                        location.user = userDataModel
                        Observable.just(location)
                    })
                }.flatMap { it }
    }

    private fun getTherapistAssignment(location: LocationDataModel): Observable<MutableMap<String, MutableList<String>>> {
        val ref = mLocationServiceGroupReference.child(location.uuid).child("service_groups")
        location.selectedServices.forEach {
            val serviceGroupRef = ref.child(it.uuid)
            it.services.map {
                RxFirebaseDatabase.data(serviceGroupRef.child(it.uuid)).toObservable()
            }.map {
                it
            }
        }
        val serviceGroupRef = mLocationServiceGroupReference.child(location.uuid).child("service_groups")
        val assignmentMap = mutableMapOf<String, MutableList<String>>()
        return RxFirebaseDatabase.data(serviceGroupRef).toObservable()
                .flatMap { Observable.fromArray(it.children.map { it.key!! }) }
                .flatMapIterable { it }
                .flatMap { RxFirebaseDatabase.data(serviceGroupRef.child(it)).toObservable() }
                .flatMap { serviceGroupSnapshot ->
                    Observable.fromArray(serviceGroupSnapshot.children.map { serviceSnapshot ->
                        val serviceKey = serviceSnapshot.key!!
                        Observable.zip(Observable.just(serviceKey), RxFirebaseDatabase.data(serviceGroupRef.child(serviceGroupSnapshot.key!!).child(serviceKey)).toObservable(), BiFunction { serviceKey: String, snapshot: DataSnapshot ->
                            assignmentMap[serviceKey] = snapshot.children.map { it.key!! }.toMutableList()
                            return@BiFunction assignmentMap
                        })
                    })
                }
                .flatMapIterable { it }
                .flatMap { it }

    }

    private fun getUser(uuid: String): Observable<UserDataModel> {
        return RxFirebaseDatabase.data(mLocationUserReference.child(uuid)).toObservable()
                .flatMap { Observable.fromArray(it.children.map { it.key!! }) }
                .flatMapIterable { it }
                .flatMap {
                    UserProvider().getUserFromUUID(it)
                }
    }

    public fun getServiceGroups(locationUUID: String): Observable<MutableList<ServiceGroupDataModel>> {
        return RxFirebaseDatabase.data(mLocationServiceGroupReference.child(locationUUID).child("service_groups")).toObservable()
                .flatMap { Observable.fromArray(it.children.map { it.key!! }) }
                .flatMapIterable { it }
                .flatMap {
                    Observable.zip(getServiceGroupFromUUID(it), getSelectedServicesFromServiceGroupUUID(locationUUID, it),
                            BiFunction { serviceGroup: ServiceGroupDataModel, services: MutableList<ServiceDataModel> ->
                                serviceGroup.services = services
                                Observable.just(serviceGroup)
                            })
                }.flatMap { it }
                .toList().toObservable()

    }

    private fun getSelectedServicesFromServiceGroupUUID(locationUUID: String, serviceGroupUUID: String): Observable<MutableList<ServiceDataModel>> {
        return RxFirebaseDatabase.data(mLocationServiceGroupReference.child(locationUUID).child("service_groups").child(serviceGroupUUID)).toObservable()
                .flatMap {
                    var servicesKey = listOf<String>()
                    if (it.exists()) {
                        servicesKey = it.children.map { it.key!! }.toList()
                    }
                    Observable.fromArray(servicesKey)
                }
                .flatMapIterable { it }
                .flatMapMaybe {
                    ServiceProvider().getServiceFromUUID(it)
                }.flatMap {
                    Observable.zip(Observable.just(it),
                            getAssignedTherapist(mLocationServiceGroupReference.child(locationUUID).child("service_groups").child(serviceGroupUUID).child(it.uuid)),
                            BiFunction { serviceDataModel: ServiceDataModel, assignedTherapistList: MutableList<String> ->
                                serviceDataModel.assignedTherapistSet = assignedTherapistList
                                serviceDataModel
                            })
                }.toList().toObservable()
    }

    private fun getAssignedTherapist(therapistAssignmentRef: DatabaseReference): Observable<MutableList<String>> {
        return Observable.zip(
                Observable.just(mutableListOf()),
                RxFirebaseDatabase.data(therapistAssignmentRef).toObservable(),
                BiFunction { therapistAssignment: MutableList<String>, dataSnapshot: DataSnapshot ->
                    dataSnapshot.children.forEach {
                        therapistAssignment.add(it.key!!)
                    }
                    therapistAssignment
                })
    }

    private fun getServiceGroupFromUUID(serviceGroupUUID: String): Observable<ServiceGroupDataModel> {
        return ServiceProvider().getServiceGroupWithoutServiceFromUUID(serviceGroupUUID)
    }

    fun removeLocation(uuid: String, onCompleteListener: OnCompleteListener<Task<Void>>) {
        mDatabaseReference.child(uuid).removeValue()
                .continueWith { mLocationServiceGroupReference.child(uuid).removeValue() }
                .continueWith { mTherapistProvider.removeTherapistFromLocation(uuid) }
                .continueWith { mLocationUserReference.child(uuid).removeValue() }
                .continueWith { mDatabaseReference.child(uuid).removeValue() }
                .addOnCompleteListener(onCompleteListener)
    }

    fun removeAnyService(serviceUUID: String): Disposable {
        return RxFirebaseDatabase.data(mLocationServiceGroupReference).subscribe({
            if (it.exists()) {
                it.children.forEach {
                    if (it.key != null)
                        mLocationServiceGroupReference.child(it.key!!).child("service_groups").child(serviceUUID).removeValue()
                }
            }
        }) {

        }
    }
}
