package com.android.nash.customer.customerservice

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.android.nash.core.CoreViewModel
import com.android.nash.customer.customerservice.customerservicedialog.CustomerServiceDialogFormData
import com.android.nash.data.CustomerDataModel
import com.android.nash.data.CustomerServiceDataModel
import com.android.nash.data.ServiceGroupDataModel
import com.android.nash.data.TherapistDataModel
import com.android.nash.provider.CustomerServiceProvider
import com.android.nash.provider.LocationProvider
import com.android.nash.provider.TherapistProvider
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

class CustomerServiceViewModel : CoreViewModel() {
    private val customerLiveData = MutableLiveData<CustomerDataModel>()
    private val customerServiceLiveData = MutableLiveData<List<CustomerServiceDataModel>>()
    private val isAddServiceSuccess = MutableLiveData<Boolean>()
    private val isLoading = MutableLiveData<Boolean>()
    private val therapistsLiveData = MutableLiveData<List<TherapistDataModel>>()
    private val serviceGroupsLiveData = MutableLiveData<List<ServiceGroupDataModel>>()
    private val totalServicePriceLiveData = MutableLiveData<Int>()

    private val mLocationProvider = LocationProvider()
    private val mTherapistProvider = TherapistProvider()
    private val mCustomerServiceProvider = CustomerServiceProvider()

    fun getCustomerServiceLiveData(): LiveData<List<CustomerServiceDataModel>> = customerServiceLiveData
    fun getCustomerLiveData(): LiveData<CustomerDataModel> = customerLiveData
    fun isAddServiceSuccess(): LiveData<Boolean> = isAddServiceSuccess
    fun isLoading(): LiveData<Boolean> = isLoading
    fun getServiceGroups(): LiveData<List<ServiceGroupDataModel>> = serviceGroupsLiveData
    fun getTherapistLiveData(): LiveData<List<TherapistDataModel>> = therapistsLiveData
    fun getTotalServicePriceLiveData(): LiveData<Int> = totalServicePriceLiveData

    fun setCustomerDataModel(customerDataModel: CustomerDataModel) {
        customerLiveData.value = customerDataModel
    }

    fun loadServiceFromCustomer() {

    }

    fun addNewService(customerServiceDataModel: CustomerServiceDataModel) {
        isLoading.value = true

        val disposable = mCustomerServiceProvider.insertCustomerTransaction(customerServiceDataModel).doOnComplete {
            isLoading.value = false
            isAddServiceSuccess.value = true
        }.subscribe({

        }) {
            it.printStackTrace()
        }
    }

    fun getFormData(): CustomerServiceDialogFormData {
        val customerServiceDialogFormData = CustomerServiceDialogFormData()
        customerServiceDialogFormData.servicesGroups = serviceGroupsLiveData.value
        customerServiceDialogFormData.therapists = therapistsLiveData.value
        customerServiceDialogFormData.customerUUID = customerLiveData.value?.uuid
        customerServiceDialogFormData.customerName = customerLiveData.value?.customerName!!
        customerServiceDialogFormData.locationUUID = getUserDataModel().value?.locationUUID!!
        return customerServiceDialogFormData
    }


    fun initData() {
        val disposable = Observable.zip(getAllTherapist(), getAllServiceGroup(), BiFunction { therapists: List<TherapistDataModel>, serviceGroups: List<ServiceGroupDataModel> ->
            therapistsLiveData.value = therapists
            serviceGroupsLiveData.value = serviceGroups
        }).subscribe()
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

    fun setLoading(isLoading: Boolean) {
        this.isLoading.value = isLoading
    }

    fun getCustomerServices() {
        val disposable = mCustomerServiceProvider.getCustomerService(customerLiveData.value?.uuid, getUserDataModel().value?.locationUUID).doOnNext {
            customerServiceLiveData.value = it
        }.subscribe({
            totalServicePriceLiveData.value = it.sumBy { it.price.toInt() }
        }) {
            it.printStackTrace()
        }
    }

}