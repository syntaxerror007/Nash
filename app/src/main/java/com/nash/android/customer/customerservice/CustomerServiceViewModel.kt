package com.nash.android.customer.customerservice

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.nash.android.core.CoreViewModel
import com.nash.android.customer.customerservice.create.CustomerServiceDialogFormData
import com.nash.android.data.CustomerDataModel
import com.nash.android.data.CustomerServiceDataModel
import com.nash.android.data.ServiceGroupDataModel
import com.nash.android.data.TherapistDataModel
import com.nash.android.provider.CustomerServiceProvider
import com.nash.android.provider.LocationProvider
import com.nash.android.provider.TherapistProvider
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

class CustomerServiceViewModel : CoreViewModel() {
    private val customerLiveData = MutableLiveData<CustomerDataModel>()
    private val customerServiceLiveData = MutableLiveData<List<CustomerServiceDataModel>>()
    private val isLoading = MutableLiveData<Boolean>()
    private val therapistsLiveData = MutableLiveData<List<TherapistDataModel>>()
    private val serviceGroupsLiveData = MutableLiveData<List<ServiceGroupDataModel>>()
    private val totalServicePriceLiveData = MutableLiveData<Int>()

    private val mLocationProvider = LocationProvider()
    private val mTherapistProvider = TherapistProvider()
    private val mCustomerServiceProvider = CustomerServiceProvider()

    fun getCustomerServiceLiveData(): LiveData<List<CustomerServiceDataModel>> = customerServiceLiveData
    fun getCustomerLiveData(): LiveData<CustomerDataModel> = customerLiveData
    fun isLoading(): LiveData<Boolean> = isLoading
    fun getTotalServicePriceLiveData(): LiveData<Int> = totalServicePriceLiveData

    fun setCustomerDataModel(customerDataModel: CustomerDataModel) {
        customerLiveData.value = customerDataModel
    }

    fun getFormData(): CustomerServiceDialogFormData {
        val customerServiceDialogFormData = CustomerServiceDialogFormData()
        customerServiceDialogFormData.customerUUID = customerLiveData.value?.uuid
        customerServiceDialogFormData.customerName = customerLiveData.value?.customerName!!
        customerServiceDialogFormData.locationUUID = getUserDataModel().value?.locationUUID!!
        return customerServiceDialogFormData
    }


    fun initData() {
        val disposable = Observable.zip(getAllTherapist(), getAllServiceGroup(), BiFunction { therapists: List<TherapistDataModel>, serviceGroups: List<ServiceGroupDataModel> ->
            therapistsLiveData.value = therapists
            serviceGroupsLiveData.value = serviceGroups
        }).subscribe({}) {
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
            return mTherapistProvider.getTherapistFromLocation(getUserDataModel().value!!.locationUUID)
        return Observable.just(mutableListOf())
    }

    fun setLoading(isLoading: Boolean) {
        this.isLoading.value = isLoading
    }

    fun getCustomerServices() {
        isLoading.value = true
        val disposable = mCustomerServiceProvider.getCustomerService(customerLiveData.value?.uuid, getUserDataModel().value?.locationUUID).doOnNext {
            customerServiceLiveData.value = it
        }.doOnComplete { isLoading.value = false }

                .subscribe({
                    totalServicePriceLiveData.value = it.sumBy { it.price.toInt() }
                }) {
                    isLoading.value = false
                    it.printStackTrace()
                }
    }

    fun getCustomerData(): CustomerDataModel = customerLiveData.value!!
}