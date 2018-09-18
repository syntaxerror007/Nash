package com.android.nash.provider

import android.app.Service
import android.util.Log
import com.android.nash.data.ServiceDataModel
import com.android.nash.data.ServiceGroupDataModel
import com.android.nash.util.SERVICE_DB
import com.android.nash.util.SERVICE_GROUP_DB
import com.android.nash.util.SERVICE_GROUP_NAME_REFERENCE_DB
import com.android.nash.util.SERVICE_NAME_DB
import com.androidhuman.rxfirebase2.database.RxFirebaseDatabase
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import io.reactivex.*
import io.reactivex.disposables.Disposable

class ServiceProvider {
    private val mFirebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val mServiceGroupDatabaseReference = mFirebaseDatabase.getReference(SERVICE_GROUP_DB)
    private val mServiceGroupNameReference = mFirebaseDatabase.getReference(SERVICE_GROUP_NAME_REFERENCE_DB)
    private val mServiceNameReference = mFirebaseDatabase.getReference(SERVICE_NAME_DB)
    private val mServiceReference = mFirebaseDatabase.getReference(SERVICE_DB)

    fun getKey(mDatabaseReference: DatabaseReference): String {
        return mDatabaseReference.push().key!!
    }


    fun insertServiceGroup(serviceGroupModel: ServiceGroupDataModel, onCompleteListener: OnCompleteListener<Void>) {
        mServiceGroupNameReference.child(serviceGroupModel.serviceGroupName).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    val uuid = getKey(mServiceGroupNameReference)
                    mServiceGroupNameReference.child(serviceGroupModel.serviceGroupName).setValue(true)
                    serviceGroupModel.uuid = uuid
                    mServiceGroupDatabaseReference.child(uuid).setValue(serviceGroupModel).addOnCompleteListener(onCompleteListener)
                } else {

                }
            }

        })
    }

    fun getAllServiceGroup(): Observable<List<ServiceGroupDataModel>> {
        return RxFirebaseDatabase.data(mServiceGroupDatabaseReference).flatMapObservable {
            val serviceGroups = it.children.mapNotNull {
                val serviceGroupDataModel = it.getValue(ServiceGroupDataModel::class.java)
                serviceGroupDataModel?.serviceKeys = it.child("services").children.map { it.key!! }.toList()
                return@mapNotNull serviceGroupDataModel
            }
            return@flatMapObservable Observable.fromArray(serviceGroups)
        }
    }

    fun getServiceGroupWithoutServiceFromUUID(uuid: String): Observable<ServiceGroupDataModel> {
        return RxFirebaseDatabase.data(mServiceGroupDatabaseReference.child(uuid)).flatMapObservable {
            Observable.just(it.getValue(ServiceGroupDataModel::class.java))
        }
    }

    fun getServiceFromUUID(uuid: String): Maybe<ServiceDataModel> {
        return RxFirebaseDatabase.data(mServiceReference.child(uuid)).flatMapMaybe {
            if (it.exists()) {
                return@flatMapMaybe Maybe.just(it.getValue(ServiceDataModel::class.java))
            }
            return@flatMapMaybe Maybe.empty<ServiceDataModel>()
        }
    }

    fun insertService(serviceGroupDataModel: ServiceGroupDataModel?, serviceDataModel: ServiceDataModel, onCompleteListener: OnCompleteListener<Task<Void>>) {
        mServiceNameReference.child(serviceDataModel.serviceName).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    if (serviceGroupDataModel != null) {
                        val uuid = getKey(mServiceReference)
                        serviceDataModel.uuid = uuid
                        mServiceReference.child(uuid).setValue(serviceDataModel).continueWith {
                            val serviceGroupRef = mServiceGroupDatabaseReference.child(serviceGroupDataModel.uuid).child("services")
                            serviceGroupRef.child(serviceDataModel.uuid).setValue(true)
                        }.continueWith {
                            mServiceGroupDatabaseReference.child(serviceGroupDataModel.uuid).child("itemCount").setValue(serviceGroupDataModel.services.size + 1)
                        }.continueWith {
                            mServiceNameReference.child(serviceDataModel.serviceName).setValue(true)
                        }.addOnCompleteListener(onCompleteListener)
                    }
                }
            }
        })
    }

    fun deleteService(serviceGroupDataModel: ServiceGroupDataModel, serviceDataModel: ServiceDataModel?, onCompleteListener: OnCompleteListener<Disposable>) {
        if (serviceDataModel != null) {
            mServiceGroupDatabaseReference.child(serviceGroupDataModel.uuid).child("services").child(serviceDataModel.uuid).removeValue().continueWith {
                mServiceGroupDatabaseReference.child(serviceGroupDataModel.uuid).child("itemCount").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists())
                            mServiceGroupDatabaseReference.child(serviceGroupDataModel.uuid).child("itemCount").runTransaction(object : Transaction.Handler {
                                override fun onComplete(p0: DatabaseError?, p1: Boolean, p2: DataSnapshot?) {

                                }

                                override fun doTransaction(p0: MutableData): Transaction.Result {
                                    val itemCount = p0.getValue(Long::class.java)
                                    if (itemCount != null) {
                                        p0.value = itemCount - 1
                                    } else {
                                        p0.value = 0
                                    }
                                    return Transaction.success(p0)
                                }

                            })
                    }

                })
            }
                    .continueWith { mServiceReference.child(serviceDataModel.uuid).removeValue() }
                    .continueWith { mServiceNameReference.child(serviceDataModel.uuid).removeValue() }
                    .continueWith { LocationProvider().removeAnyService(serviceDataModel.uuid) }
                    .addOnCompleteListener { onCompleteListener.onComplete(it) }

        }
    }

    fun updateServiceGroup(oldServiceGroupName: String, serviceGroupDataModel: ServiceGroupDataModel, onCompleteListener: OnCompleteListener<Task<Void>>) {
        val newUuid = getKey(mServiceGroupDatabaseReference)
        serviceGroupDataModel.uuid = newUuid
        mServiceGroupDatabaseReference.child(serviceGroupDataModel.uuid).setValue(serviceGroupDataModel).continueWith { _ ->
            val newServiceDatabaseRef = mServiceGroupDatabaseReference.child(serviceGroupDataModel.uuid).child("services")
            serviceGroupDataModel.services.forEach {
                newServiceDatabaseRef.child(it.uuid).setValue(true)
            }
        }.continueWith {
            mServiceGroupDatabaseReference.child(oldServiceGroupName).removeValue()
        }.addOnCompleteListener(onCompleteListener)
    }

    fun findServiceGroup(uuid: String): Observable<ServiceGroupDataModel> {
        return RxFirebaseDatabase.data(mServiceGroupDatabaseReference.child(uuid)).map {
            val serviceGroupDataModel = it.getValue(ServiceGroupDataModel::class.java)
            serviceGroupDataModel?.serviceKeys = it.child("services").children.map { it.key!! }.toList()
            return@map serviceGroupDataModel
        }.flatMapObservable { getServiceFromServiceGroup(it) }
    }

    private fun findService(uuid: String): Observable<ServiceDataModel> {
        return RxFirebaseDatabase.data(mServiceReference.child(uuid)).map { it.getValue(ServiceDataModel::class.java)!! }.toObservable()
    }

    fun getServiceFromServiceGroup(serviceGroupDataModel: ServiceGroupDataModel): Observable<ServiceGroupDataModel> {
        return Observable.fromIterable(serviceGroupDataModel.serviceKeys).flatMap {
            findService(it)
        }.toList().flatMapObservable {
            serviceGroupDataModel.services = it
            serviceGroupDataModel.items = it
            return@flatMapObservable Observable.just(serviceGroupDataModel)
        }
    }
}
