package com.android.nash.provider

import com.android.nash.data.CustomerServiceDataModel
import com.android.nash.data.ServiceDataModel
import com.android.nash.data.ServiceGroupDataModel
import com.android.nash.data.TherapistDataModel
import com.android.nash.util.CUSTOMER_SERVICE_DB
import com.android.nash.util.LOCATION_THERAPIST_DB
import com.android.nash.util.SERVICE_TRANSACTION_DB
import com.android.nash.util.THERAPIST_DB
import com.androidhuman.rxfirebase2.database.RxFirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.functions.Function4

class CustomerServiceProvider {
    val mFirebaseDatabase = FirebaseDatabase.getInstance()
    private val mCustomerServiceDatabaseRef: DatabaseReference = mFirebaseDatabase.getReference(CUSTOMER_SERVICE_DB)
    private val mServiceTransactionDatabaseRef: DatabaseReference = mFirebaseDatabase.getReference(SERVICE_TRANSACTION_DB)
    private val mServiceProvider = ServiceProvider()
    private val mTherapistProvider = TherapistProvider()

    fun getKey(databaseReference: DatabaseReference): String = databaseReference.push().key!!
    fun insertCustomerService(customerServiceDataModel: CustomerServiceDataModel): Completable = with(customerServiceDataModel) {
        uuid = getKey(mCustomerServiceDatabaseRef.child(customerUUID))
        return RxFirebaseDatabase.setValue(mCustomerServiceDatabaseRef.child(customerUUID).child(uuid), customerServiceDataModel)
    }

    fun getCustomerService(customerUUID: String?): Observable<List<CustomerServiceDataModel>> =
            if (customerUUID != null) {
                RxFirebaseDatabase.data(mCustomerServiceDatabaseRef.child(customerUUID)).flatMapObservable {
                    if (it.exists())
                        Observable.fromArray(it.children.mapNotNull { return@mapNotNull it.getValue(CustomerServiceDataModel::class.java) })
                    else
                        Observable.just(listOf())
                }
                        .flatMapIterable { it }
                        .flatMap {
                            Observable.zip(
                                    Observable.just(it),
                                    mServiceProvider.getServiceGroupWithoutServiceFromUUID(it.serviceGroupUUID),
                                    mServiceProvider.getServiceFromUUID(it.serviceUUID).toObservable(),
                                    mTherapistProvider.getTherapist(it.therapistUUID).toObservable(),
                                    Function4<CustomerServiceDataModel, ServiceGroupDataModel, ServiceDataModel, TherapistDataModel, CustomerServiceDataModel> { customerServiceDataModel, serviceGroupDataModel, serviceDataModel, therapistDataModel ->
                                        customerServiceDataModel.serviceGroup = serviceGroupDataModel
                                        customerServiceDataModel.service = serviceDataModel
                                        customerServiceDataModel.therapist = therapistDataModel
                                        customerServiceDataModel
                                    }
                            )
                        }
                        .toList()
                        .toObservable()
            } else {
                Observable.just(listOf())
            }

}