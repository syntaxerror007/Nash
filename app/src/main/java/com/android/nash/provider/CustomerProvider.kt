package com.android.nash.provider

import com.android.nash.data.CustomerDataModel
import com.android.nash.util.CUSTOMER_DB
import com.androidhuman.rxfirebase2.database.RxFirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import io.reactivex.Completable
import io.reactivex.Observable

class CustomerProvider {
    private val mFirebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val mCustomerDatabaseRef: DatabaseReference = mFirebaseDatabase.getReference(CUSTOMER_DB)
    val totalItemPerPage = 50

    fun getKey(databaseReference: DatabaseReference): String = databaseReference.push().key!!

    fun insertCustomer(customerDataModel: CustomerDataModel): Completable = with(customerDataModel) {
        if (uuid.isBlank())
            uuid = getKey(mCustomerDatabaseRef)
        return RxFirebaseDatabase.setValue(mCustomerDatabaseRef.child(uuid), customerDataModel)
    }

    fun getCustomerByUUID(uuid: String): Observable<CustomerDataModel> =
            RxFirebaseDatabase.data(mCustomerDatabaseRef.child(uuid))
                    .flatMapObservable {
                        Observable.just(it.getValue(CustomerDataModel::class.java))
                    }

    fun updateCustomer(customerDataModel: CustomerDataModel): Completable = with(customerDataModel) {
        val oldUUID = uuid
        customerDataModel.uuid = getKey(mCustomerDatabaseRef)
        return insertCustomer(customerDataModel).doOnComplete { deleteCustomer(oldUUID) }
    }

    fun deleteCustomer(uuid: String): Completable = RxFirebaseDatabase.removeValue(mCustomerDatabaseRef.child(uuid))

    fun getAllCustomer() = getAllCustomer(null)

    fun getAllCustomer(lastLoadedItemUUID: String?): Observable<List<CustomerDataModel>> {
        val ref: Query = if (lastLoadedItemUUID.isNullOrEmpty()) {
            mCustomerDatabaseRef.limitToFirst(totalItemPerPage)
        } else {
            mCustomerDatabaseRef.orderByChild("uuid").startAt(lastLoadedItemUUID).limitToFirst(totalItemPerPage)
        }
        return RxFirebaseDatabase.data(ref).flatMapObservable {
            if (it.exists()) {
                val temp = it.children.mapNotNull { return@mapNotNull it.getValue(CustomerDataModel::class.java) }
                if (lastLoadedItemUUID != null)
                    return@flatMapObservable Observable.fromArray(temp.drop(1))
                Observable.fromArray(temp)
            } else
                Observable.just(listOf())
        }
    }

    fun searchCustomer(inputtedText: String): Observable<List<CustomerDataModel>> {
        return RxFirebaseDatabase.data(mCustomerDatabaseRef.orderByChild("customerLowerCase").startAt(inputtedText.toLowerCase()).endAt("${inputtedText.toLowerCase()}\uf8ff").limitToFirst(100)).flatMapObservable {
            if (it.exists())
                Observable.fromArray(it.children.mapNotNull { return@mapNotNull it.getValue(CustomerDataModel::class.java) })
            else
                Observable.just(listOf())
        }
    }
}