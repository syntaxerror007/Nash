package com.android.nash.reminder

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

    private val mCustomerServiceProvider = CustomerServiceProvider()
    private val mLocationProvider = LocationProvider()
    private val mTherapistProvider = TherapistProvider()

    fun getToRemindCustomer(currentTimestamp: Long) {
        val disposable = Observable.zip(getAllServiceGroup(), getAllTherapist(), BiFunction { serviceGroups: List<ServiceGroupDataModel>, therapists: List<TherapistDataModel> ->
            therapistsLiveData.value = therapists.toList()
            serviceGroupsLiveData.value = serviceGroups.toList()
            true
        }).map { isSuccess ->
            if (isSuccess) {
                return@map mCustomerServiceProvider.getCustomerServiceToRemind(currentTimestamp, getUserDataModel().value!!.locationUUID)
            } else {
                return@map Observable.just(listOf<CustomerServiceDataModel>())
            }
        }.flatMap { it }.subscribe({
            val temp = it
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
            return mTherapistProvider.getTherapistFromLocation(getUserDataModel().value!!.locationUUID).toObservable()
        return Observable.just(mutableListOf())
    }
}