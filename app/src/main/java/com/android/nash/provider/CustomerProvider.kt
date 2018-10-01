package com.android.nash.provider

import com.android.nash.data.CustomerDataModel
import com.android.nash.data.CustomerServiceDataModel
import com.android.nash.util.CUSTOMER_DB
import com.android.nash.util.CUSTOMER_SERVICE_DB
import com.android.nash.util.SERVICE_TRANSACTION_DB
import com.androidhuman.rxfirebase2.database.RxFirebaseDatabase
import com.facebook.stetho.inspector.protocol.module.Database
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Completable
import io.reactivex.Observable

class CustomerProvider {
    private val mFirebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val mCustomerDatabaseRef: DatabaseReference = mFirebaseDatabase.getReference(CUSTOMER_DB)
    private val mCustomerServiceDatabaseRef: DatabaseReference = mFirebaseDatabase.getReference(CUSTOMER_SERVICE_DB)
    private val mServiceTransactionDatabaseRef: DatabaseReference = mFirebaseDatabase.getReference(SERVICE_TRANSACTION_DB)

    fun getKey(databaseReference: DatabaseReference): String = databaseReference.push().key!!

    fun insertCustomer(customerDataModel: CustomerDataModel): Completable = with(customerDataModel) {
        uuid = getKey(mCustomerDatabaseRef)
        return RxFirebaseDatabase.setValue(mCustomerDatabaseRef.child(uuid), customerDataModel)
    }

    fun updateCustomer(customerDataModel: CustomerDataModel): Completable = with(customerDataModel) {
        val oldUUID = uuid
        customerDataModel.uuid = getKey(mCustomerDatabaseRef)
        return insertCustomer(customerDataModel).doOnComplete { deleteCustomer(oldUUID) }
    }

    fun deleteCustomer(uuid: String): Completable = RxFirebaseDatabase.removeValue(mCustomerDatabaseRef.child(uuid))

    fun getAllCustomer(): Observable<List<CustomerDataModel>> {
        return searchCustomer("")
    }

    fun searchCustomer(inputtedText: String): Observable<List<CustomerDataModel>> {
        return RxFirebaseDatabase.data(mCustomerDatabaseRef.orderByChild("customerName").startAt("%$inputtedText%").endAt("$inputtedText\uf8ff")).flatMapObservable {
            if (it.exists())
                Observable.fromArray(it.children.mapNotNull { return@mapNotNull it.getValue(CustomerDataModel::class.java) })
            else
                Observable.just(listOf())
        }
    }

    fun insertCustomerService(customerServiceDataModel: CustomerServiceDataModel): Completable = with(customerServiceDataModel) {
        uuid = getKey(mServiceTransactionDatabaseRef)
        return RxFirebaseDatabase.setValue(mCustomerServiceDatabaseRef.child(customerUUID).child(uuid), customerServiceDataModel)
    }

    fun getCustomerService(customerUUID: String): Observable<List<CustomerServiceDataModel>> {
        return RxFirebaseDatabase.data(mCustomerServiceDatabaseRef.child(customerUUID)).flatMapObservable {
            if (it.exists())
                Observable.fromArray(it.children.mapNotNull { return@mapNotNull it.getValue(CustomerServiceDataModel::class.java) })
            else
                Observable.just(listOf())
        }
    }
}