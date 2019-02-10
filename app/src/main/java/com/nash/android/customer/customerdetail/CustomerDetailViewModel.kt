package com.nash.android.customer.customerdetail

import android.arch.lifecycle.MutableLiveData
import com.nash.android.core.CoreViewModel
import com.nash.android.data.CustomerDataModel

class CustomerDetailViewModel : CoreViewModel() {
    private val customerDataModelLiveData = MutableLiveData<CustomerDataModel>()

    fun setCustomerDataModel(customerDataModel: CustomerDataModel) {
        customerDataModelLiveData.value = customerDataModel
    }

    fun getCustomerData(): CustomerDataModel = customerDataModelLiveData.value!!
}