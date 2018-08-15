package com.android.nash.provider

import com.android.nash.data.ServiceDataModel
import com.android.nash.data.ServiceGroupDataModel
import com.android.nash.service.form.data.ServiceGroupModel
import com.android.nash.service.form.data.ServiceModel
import com.android.nash.util.SERVICE_DB
import com.android.nash.util.SERVICE_GROUP_DB
import com.androidhuman.rxfirebase2.database.RxFirebaseDatabase
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Observable


class ServiceProvider {
    private val mFirebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val mDatabaseReference = mFirebaseDatabase.getReference(SERVICE_GROUP_DB)

    fun insertServiceGroup(serviceGroupModel: ServiceGroupModel, onCompleteListener: OnCompleteListener<Void>) {
        val uuid = mDatabaseReference.push().key
        serviceGroupModel.uuid = uuid!!
        mDatabaseReference.child(uuid).setValue(serviceGroupModel).addOnCompleteListener(onCompleteListener)
    }

    fun getAllServiceGroup(): Observable<List<ServiceGroupDataModel>> {
        return RxFirebaseDatabase.data(mDatabaseReference).flatMapObservable {
            val locations = it.children.mapNotNull{ it.getValue(ServiceGroupModel::class.java)}
            return@flatMapObservable Observable.fromArray(locations)
        }.flatMap {
            val listServiceGroupDataModel = mutableListOf<ServiceGroupDataModel>()
            for (serviceGroupDataModel in it) {
                listServiceGroupDataModel.add(getServiceGroupDataModelFromServiceGroupModel(serviceGroupDataModel))
            }
            return@flatMap Observable.fromArray(listServiceGroupDataModel)
        }
    }

    fun insertService(serviceDataModel: ServiceModel, onCompleteListener: OnCompleteListener<Void>) {
        val serviceDatabaseReference = mFirebaseDatabase.getReference(SERVICE_DB)
        val uuid = serviceDatabaseReference.push().key
        serviceDataModel.id = uuid!!
        serviceDatabaseReference.child(uuid).setValue(serviceDataModel).addOnCompleteListener(onCompleteListener)
    }

    private fun getServiceGroupDataModelFromServiceGroupModel(serviceGroupDataModel: ServiceGroupModel): ServiceGroupDataModel {
        val listServiceModel = mutableListOf<ServiceDataModel>()
        for (serviceDataModel in serviceGroupDataModel.services) {
            listServiceModel.add(getServiceDataModelFromServiceModel(serviceDataModel))
        }
        return ServiceGroupDataModel(serviceGroupDataModel.uuid, serviceGroupDataModel.serviceGroupName, listServiceModel)
    }

    private fun getServiceDataModelFromServiceModel(serviceDataModel: ServiceModel): ServiceDataModel {
        return ServiceDataModel(id = serviceDataModel.id, serviceName = serviceDataModel.serviceName, price = serviceDataModel.price, reminder = serviceDataModel.reminder, shouldFreeText = serviceDataModel.shouldFreeText)
    }

    fun updateServiceGroup(serviceGroupModel: ServiceGroupModel, onCompleteListener: OnCompleteListener<Void>) {
        val uuid = serviceGroupModel.uuid
        mDatabaseReference.child(uuid).setValue(serviceGroupModel).addOnCompleteListener(onCompleteListener)
    }

    fun getServiceModelFromServiceDataModel(serviceDataModel: ServiceDataModel): ServiceModel {
        return ServiceModel(id = serviceDataModel.id, serviceName = serviceDataModel.serviceName, price = serviceDataModel.price, reminder = serviceDataModel.reminder, shouldFreeText = serviceDataModel.shouldFreeText)
    }

    fun getServiceGroupModelFromServiceGroupDataModel(serviceGroupDataModel: ServiceGroupDataModel): ServiceGroupModel {
        val listServiceModel = mutableListOf<ServiceModel>()
        for (serviceDataModel in serviceGroupDataModel.services) {
            listServiceModel.add(getServiceModelFromServiceDataModel(serviceDataModel))
        }
        return ServiceGroupModel(serviceGroupDataModel.uuid, serviceGroupDataModel.serviceGroupName, listServiceModel)
    }
}
