package com.android.nash.provider

import com.android.nash.data.CustomerDataModel
import com.android.nash.util.CUSTOMER_DB
import com.androidhuman.rxfirebase2.database.RxFirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Completable
import io.reactivex.Observable

class CustomerProvider {
    private val mFirebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val mCustomerDatabaseRef: DatabaseReference = mFirebaseDatabase.getReference(CUSTOMER_DB)

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
        return RxFirebaseDatabase.data(mCustomerDatabaseRef).flatMapObservable {
            if (it.exists())
                Observable.fromArray(it.children.mapNotNull { return@mapNotNull it.getValue(CustomerDataModel::class.java) })
            else
                Observable.just(null)
        }
    }

//    fun doSomething() {
//        val disposable = insertCustomer(CustomerDataModel()).subscribeWith(object: DisposableCompletableObserver() {
//            override fun onComplete() {
//
//            }
//
//            override fun onError(e: Throwable) {
//
//            }
//        })
//    }
}