package com.nash.android.customer.customerservice.create

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.nash.android.core.CoreViewModel
import com.nash.android.data.CustomerDataModel
import com.nash.android.data.CustomerServiceDataModel
import com.nash.android.data.ServiceGroupDataModel
import com.nash.android.data.TherapistDataModel
import com.nash.android.provider.CustomerServiceProvider
import com.nash.android.provider.LocationProvider
import com.nash.android.provider.TherapistProvider
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

class CustomerAddServiceFormViewModel : CoreViewModel() {
    var locationUUID: String? = null
    var customerName: String? = null
    var customerUUID: String? = null

    private val customerLiveData = MutableLiveData<CustomerDataModel>()
    private val customerServiceLiveData = MutableLiveData<List<CustomerServiceDataModel>>()
    private val isAddServiceSuccess = MutableLiveData<Boolean>()
    private val isLoading = MutableLiveData<Boolean>()
    private val therapistsLiveData = MutableLiveData<List<TherapistDataModel>>()
    private val serviceGroupsLiveData = MutableLiveData<List<ServiceGroupDataModel>>()

    private val mLocationProvider = LocationProvider()
    private val mTherapistProvider = TherapistProvider()
    private val mCustomerServiceProvider = CustomerServiceProvider()

    fun getCustomerServiceLiveData(): LiveData<List<CustomerServiceDataModel>> = customerServiceLiveData
    fun getCustomerLiveData(): LiveData<CustomerDataModel> = customerLiveData
    fun isAddServiceSuccess(): LiveData<Boolean> = isAddServiceSuccess
    fun isLoading(): LiveData<Boolean> = isLoading
    fun getServiceGroups(): LiveData<List<ServiceGroupDataModel>> = serviceGroupsLiveData
    fun getTherapistLiveData(): LiveData<List<TherapistDataModel>> = therapistsLiveData


    fun setFormData(formData: CustomerServiceDialogFormData) {
        locationUUID = formData.locationUUID
        customerName = formData.customerName
        customerUUID = formData.customerUUID
    }

    fun registerServiceToCustomer(customerServiceDataModel: CustomerServiceDataModel) {
        isLoading.value = true

        val disposable = mCustomerServiceProvider.insertCustomerTransaction(customerServiceDataModel).doOnComplete {
            isLoading.value = false
            isAddServiceSuccess.value = true
        }.subscribe({

        }) {
            it.printStackTrace()
        }
    }


    fun initData() {
        isLoading.value = true
        val disposable = Observable.zip(getAllTherapist(), getAllServiceGroup(), BiFunction { therapists: List<TherapistDataModel>, serviceGroups: List<ServiceGroupDataModel> ->
            therapistsLiveData.value = therapists
            serviceGroupsLiveData.value = serviceGroups
            isLoading.value = false
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

    fun setLoading(b: Boolean) {
        isLoading.value = b
    }
}