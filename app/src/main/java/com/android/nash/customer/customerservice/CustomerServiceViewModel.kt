package com.android.nash.customer.customerservice

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.android.nash.core.CoreViewModel
import com.android.nash.customer.customerservice.customerservicedialog.CustomerServiceDialogFormData
import com.android.nash.data.*
import com.android.nash.provider.LocationProvider
import com.android.nash.provider.ServiceProvider
import com.android.nash.provider.TherapistProvider
import io.reactivex.Observable
import io.reactivex.functions.Consumer

class CustomerServiceViewModel : CoreViewModel() {
    private val customerLiveData = MutableLiveData<CustomerDataModel>()
    private val customerServiceLiveData = MutableLiveData<List<CustomerServiceDataModel>>()
    private val isAddServiceSuccess = MutableLiveData<Boolean>()
    private val isLoading = MutableLiveData<Boolean>()
    private val therapistsLiveData = MutableLiveData<List<TherapistDataModel>>()
    private val serviceGroupsLiveData = MutableLiveData<List<ServiceGroupDataModel>>()

    private val mLocationProvider = LocationProvider()
    private val mTherapistProvider = TherapistProvider()

    fun getCustomerServiceLiveData(): LiveData<List<CustomerServiceDataModel>> = customerServiceLiveData
    fun getCustomerLiveData(): LiveData<CustomerDataModel> = customerLiveData
    fun isAddServiceSuccess(): LiveData<Boolean> = isAddServiceSuccess
    fun isLoading(): LiveData<Boolean> = isLoading
    fun getServiceGroups(): LiveData<List<ServiceGroupDataModel>> = serviceGroupsLiveData
    fun getTherapistLiveData(): LiveData<List<TherapistDataModel>> = therapistsLiveData
    fun getTherapist(serviceUUID: String): List<TherapistDataModel>? =
            therapistsLiveData.value?.asSequence()?.filter { therapistDataModel -> therapistDataModel.assignmentSet.contains(serviceUUID) }?.toList()

    fun setCustomerDataModel(customerDataModel: CustomerDataModel) {
        customerLiveData.value = customerDataModel
    }

    fun loadServiceFromCustomer() {

    }

    fun addNewService(customerServiceDataModel: CustomerServiceDataModel) {
        isLoading.value = true
    }

    fun getFormData(): CustomerServiceDialogFormData {
        val customerServiceDialogFormData = CustomerServiceDialogFormData()
        customerServiceDialogFormData.servicesGroups = serviceGroupsLiveData.value
        customerServiceDialogFormData.therapists = therapistsLiveData.value
        return customerServiceDialogFormData
    }


    fun initData() {
        val disposable = getAllTherapist().flatMap {
            therapistsLiveData.value = it.toList()
            getAllServiceGroup()
        }.map {
            serviceGroupsLiveData.value = it.toList()
        }.subscribe {

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

    fun setLoading(isLoading: Boolean) {
        this.isLoading.value = isLoading
    }

}