package com.android.nash.provider

import com.android.nash.data.ServiceDataModel
import com.android.nash.data.ServiceGroupDataModel
import com.android.nash.service.form.data.ServiceGroupModel
import com.android.nash.service.form.data.ServiceModel
import com.android.nash.util.SERVICE_DB
import com.android.nash.util.SERVICE_GROUP_DB
import com.android.nash.util.SERVICE_GROUP_NAME_REFERENCE_DB
import com.androidhuman.rxfirebase2.database.RxFirebaseDatabase
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.*
import io.reactivex.Observable


class ServiceProvider {
    private val mFirebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val mDatabaseReference = mFirebaseDatabase.getReference(SERVICE_GROUP_DB)
    private val mServiceGroupNameReference = mFirebaseDatabase.getReference(SERVICE_GROUP_NAME_REFERENCE_DB)
    private val mServiceNameReference = mFirebaseDatabase.getReference(SERVICE_DB)

    fun insertServiceGroup(serviceGroupModel: ServiceGroupDataModel, onCompleteListener: OnCompleteListener<Void>) {
        mServiceGroupNameReference.child(serviceGroupModel.serviceGroupName).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    mServiceGroupNameReference.child(serviceGroupModel.serviceGroupName).setValue(true)
                    mDatabaseReference.child(serviceGroupModel.serviceGroupName).setValue(serviceGroupModel).addOnCompleteListener(onCompleteListener)
                } else {

                }
            }

        })
    }

    fun getAllServiceGroup(): Observable<List<ServiceGroupDataModel>> {
        return RxFirebaseDatabase.data(mDatabaseReference).flatMapObservable {
            val serviceGroups = it.children.mapNotNull{
                val serviceGroupDataModel = it.getValue(ServiceGroupDataModel::class.java)
                serviceGroupDataModel?.services = it.child("services").children.mapNotNull { it.getValue(ServiceDataModel::class.java) }.toMutableList()
                serviceGroupDataModel?.items = serviceGroupDataModel?.services
                return@mapNotNull serviceGroupDataModel
            }
            return@flatMapObservable Observable.fromArray(serviceGroups)
        }
    }

    fun insertService(serviceGroupDataModel: ServiceGroupDataModel?, serviceDataModel: ServiceDataModel, onCompleteListener: OnCompleteListener<Void>) {
        mServiceNameReference.child(serviceDataModel.serviceName).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    if (serviceGroupDataModel != null) {
                        mDatabaseReference.child(serviceGroupDataModel.serviceGroupName).child("services").child(serviceDataModel.serviceName).setValue(serviceDataModel).addOnCompleteListener {
                            mDatabaseReference.child(serviceGroupDataModel.serviceGroupName).child("itemCount").addListenerForSingleValueEvent(object: ValueEventListener{
                                override fun onCancelled(p0: DatabaseError) {

                                }

                                override fun onDataChange(p0: DataSnapshot) {
                                    if (p0.exists()) {
                                        val prevCount = p0.value as Long
                                        mDatabaseReference.child(serviceGroupDataModel.serviceGroupName).child("itemCount").setValue(prevCount+1)
                                        mServiceNameReference.child(serviceDataModel.serviceName).setValue(true)
                                        onCompleteListener.onComplete(it)
                                    }
                                }

                            })
                        }
                    }
                }
            }

        })
    }
}
