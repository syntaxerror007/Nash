package com.android.nash.reminder

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.android.nash.core.CoreViewModel
import com.android.nash.data.CustomerServiceDataModel
import com.android.nash.data.ServiceGroupDataModel
import com.android.nash.data.TherapistDataModel
import com.android.nash.provider.CustomerServiceProvider
import com.android.nash.provider.LocationProvider
import com.android.nash.provider.TherapistProvider
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

class CustomerReminderViewModel : CoreViewModel() {
    private val therapistsLiveData = MutableLiveData<List<TherapistDataModel>>()
    private val serviceGroupsLiveData = MutableLiveData<List<ServiceGroupDataModel>>()
    private val customerServiceLiveData = MutableLiveData<List<CustomerServiceDataModel>>()
    private val isLoading = MutableLiveData<Boolean>()
    private val isDataChanged = MutableLiveData<Boolean>()

    private val mCustomerServiceProvider = CustomerServiceProvider()
    private val mLocationProvider = LocationProvider()
    private val mTherapistProvider = TherapistProvider()


    fun getCustomerServiceLiveData(): LiveData<List<CustomerServiceDataModel>> = customerServiceLiveData
    fun isLoading(): LiveData<Boolean> = isLoading
    fun isDataChanged(): LiveData<Boolean> = isDataChanged


    fun getToRemindCustomer(currentTimestamp: Long) {
        isLoading.value = true
        val disposable = Observable.zip(getAllServiceGroup(), getAllTherapist(), BiFunction { serviceGroups: List<ServiceGroupDataModel>, therapists: List<TherapistDataModel> ->
            therapistsLiveData.value = therapists.toList()
            serviceGroupsLiveData.value = serviceGroups.toList()
            true
        }).map { isSuccess ->
            if (isSuccess) {
                return@map mCustomerServiceProvider.getCustomerServiceToRemind(currentTimestamp, getUserDataModel().value!!.locationUUID, therapistsLiveData.value!!, serviceGroupsLiveData.value!!)
            } else {
                return@map Observable.just(listOf<CustomerServiceDataModel>())
            }
        }.flatMap { it }.subscribe({
            customerServiceLiveData.value = it
            isLoading.value = false
        }) {
            it.printStackTrace()
        }
    }


    private fun getAllServiceGroup(): Observable<MutableList<ServiceGroupDataModel>> {
        if (getUserDataModel().value != null)
            return mLocationProvider.getServiceGroups(getUserDataModel().value!!.locationUUID)
        return Observable.just(mutableListOf())
    }


    private fun getAllTherapist(): Observable<MutableList<TherapistDataModel>> {
        if (getUserDataModel().value != null)
            return mTherapistProvider.getAllTherapist()
        return Observable.just(mutableListOf())
    }

    fun setCustomerHasReminded(customerServiceDataModel: CustomerServiceDataModel, checked: Boolean) {
        isLoading.value = true
        val disposableCompletableObserver = mCustomerServiceProvider.setCustomerHasReminded(customerServiceDataModel, checked).doOnComplete {
            isLoading.value = false
            isDataChanged.value = true
        }.subscribe()
    }
}