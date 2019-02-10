package com.nash.android.provider

import com.androidhuman.rxfirebase2.database.RxFirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.nash.android.data.*
import com.nash.android.util.CUSTOMER_SERVICE_DB
import com.nash.android.util.SERVICE_TRANSACTION_DB
import com.nash.android.util.SERVICE_TRANSACTION_TIMESTAMP_DB
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function5

class CustomerServiceProvider {
    val mFirebaseDatabase = FirebaseDatabase.getInstance()
    private val mCustomerServiceDatabaseRef: DatabaseReference = mFirebaseDatabase.getReference(CUSTOMER_SERVICE_DB)
    private val mServiceTransactionDatabaseRef: DatabaseReference = mFirebaseDatabase.getReference(SERVICE_TRANSACTION_DB)
    private val mServiceToRemindTimestampDatabaseRef: DatabaseReference = mFirebaseDatabase.getReference(SERVICE_TRANSACTION_TIMESTAMP_DB)
    private val mServiceProvider = ServiceProvider()
    private val mTherapistProvider = TherapistProvider()
    private val mLocationProvider = LocationProvider()
    private val mCustomerProvider = CustomerProvider()


    fun getKey(databaseReference: DatabaseReference): String = databaseReference.push().key!!
    fun insertCustomerTransaction(customerServiceDataModel: CustomerServiceDataModel): Completable = with(customerServiceDataModel) {
        uuid = getKey(mServiceTransactionDatabaseRef.child(customerServiceDataModel.customerUUID))
        RxFirebaseDatabase.setValue(mServiceTransactionDatabaseRef.child(customerServiceDataModel.customerUUID).child(uuid), customerServiceDataModel)
    }

    fun getCustomerService(customerUUID: String?, locationUUID: String?): Observable<List<CustomerServiceDataModel>> =
            if (customerUUID != null) {
                RxFirebaseDatabase.data(mServiceTransactionDatabaseRef.child(customerUUID).orderByChild("treatmentDateTimestamp")).toObservable().concatMap {
                    if (it.exists())
                        Observable.fromArray(it.children.mapNotNull { return@mapNotNull it.getValue(CustomerServiceDataModel::class.java) })
                    else
                        Observable.just(listOf())
                }
                        .concatMapIterable { it }
                        .concatMap {
                            Observable.zip(
                                    Observable.just(it),
                                    mServiceProvider.getServiceGroupWithoutServiceFromUUID(it.serviceGroupUUID).toObservable(),
                                    mServiceProvider.getServiceFromUUID(it.serviceUUID).toObservable(),
                                    mTherapistProvider.getTherapist(it.therapistUUID).toObservable(),
                                    mLocationProvider.getLocationName(it.locationUUID).toObservable(),
                                    Function5<CustomerServiceDataModel, ServiceGroupDataModel, ServiceDataModel, TherapistDataModel, String, CustomerServiceDataModel> { customerServiceDataModel, serviceGroupDataModel, serviceDataModel, therapistDataModel, locationName ->
                                        customerServiceDataModel.serviceGroup = serviceGroupDataModel
                                        customerServiceDataModel.service = serviceDataModel
                                        customerServiceDataModel.therapist = therapistDataModel
                                        customerServiceDataModel.locationName = locationName
                                        customerServiceDataModel
                                    }
                            )
                        }
                        .toList()
                        .flatMapObservable {
                            it.reverse()
                            Observable.just(it)
                        }
            } else {
                Observable.just(listOf())
            }

    fun getCustomerServiceToRemind(timestamp: Long, locationUUID: String, therapists: List<TherapistDataModel>, serviceGroups: List<ServiceGroupDataModel>): Observable<List<CustomerServiceDataModel>> {
        return RxFirebaseDatabase.data(mServiceTransactionDatabaseRef).flatMapObservable {
            if (it.exists()) {
                return@flatMapObservable Observable.fromArray(it.children.mapNotNull { return@mapNotNull it.key })
            } else {
                return@flatMapObservable Observable.just(listOf<String>())
            }
        }
                .flatMapIterable { it }
                .flatMap {
                    getCustomerServiceDataModel(it, timestamp, therapists, serviceGroups)
                }
                .flatMapIterable { it }
                .map { it }
                .toList()
                .map {
                    it.sortedWith(kotlin.Comparator { cust1, cust2 ->
                        (cust2.toRemindDateTimestamp - cust1.toRemindDateTimestamp).toInt()
                    })
                }.toObservable()
    }

    private fun getCustomerServiceDataModel(customerUUID: String, timestamp: Long, therapists: List<TherapistDataModel>, serviceGroups: List<ServiceGroupDataModel>): Observable<MutableList<CustomerServiceDataModel>>? {
        return RxFirebaseDatabase.data(mServiceTransactionDatabaseRef.child(customerUUID)).flatMapObservable {
            if (it.exists()) {
                return@flatMapObservable Observable.fromArray(it.children.mapNotNull { return@mapNotNull it.getValue(CustomerServiceDataModel::class.java) })
            } else {
                return@flatMapObservable Observable.just(listOf<CustomerServiceDataModel>())
            }
        }.flatMapIterable { it }
                .filter { it.toRemindDateTimestamp < timestamp && it.hasReminded.not() }
                .flatMap {
                    Observable.zip(Observable.just(it), mCustomerProvider.getCustomerByUUID(it.customerUUID),
                            BiFunction { customerServiceDataModel: CustomerServiceDataModel, customerDataModel: CustomerDataModel ->
                                customerServiceDataModel.customerDataModel = customerDataModel
                                customerServiceDataModel
                            })
                }
                .flatMap {
                    val therapist = findTherapist(therapists, it)
                    if (therapist != null)
                        it.therapist = therapist
                    val serviceGroup = findServiceGroup(serviceGroups, it)
                    if (serviceGroup != null)
                        it.serviceGroup = serviceGroup
                    val service = findService(it)
                    if (service != null)
                        it.service = service
                    Observable.just(it)
                }.toList().toObservable()
    }

    private fun findService(it: CustomerServiceDataModel): ServiceDataModel? {
        return it.serviceGroup?.services?.find { serviceDataModel ->
            it.serviceUUID == serviceDataModel.uuid
        }
    }

    private fun findTherapist(therapists: List<TherapistDataModel>, it: CustomerServiceDataModel): TherapistDataModel? {
        return therapists.find { therapistDataModel ->
            it.therapistUUID == therapistDataModel.uuid
        }
    }

    private fun findServiceGroup(serviceGroups: List<ServiceGroupDataModel>, it: CustomerServiceDataModel): ServiceGroupDataModel? {
        return serviceGroups.find { serviceGroupDataModel ->
            it.serviceGroupUUID == serviceGroupDataModel.uuid
        }
    }

    fun setCustomerHasReminded(customerServiceDataModel: CustomerServiceDataModel, checked: Boolean): Completable {
        return RxFirebaseDatabase.setValue(mServiceTransactionDatabaseRef.child(customerServiceDataModel.customerUUID).child(customerServiceDataModel.uuid).child("hasReminded"), (checked))
    }
}