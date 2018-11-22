package com.android.nash.customer.customerdetail

import android.arch.lifecycle.MutableLiveData
import com.android.nash.core.CoreViewModel
import com.android.nash.data.CustomerDataModel

class CustomerDetailViewModel : CoreViewModel() {
    private val customerDataModelLiveData = MutableLiveData<CustomerDataModel>()

    fun setCustomerDataModel(customerDataModel: CustomerDataModel) {
        customerDataModelLiveData.value = customerDataModel
    }

    fun getCustomerData(): CustomerDataModel = customerDataModelLiveData.value!!
}