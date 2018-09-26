package com.android.nash.customer.customersearch

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.android.nash.core.CoreViewModel
import com.android.nash.data.CustomerDataModel
import com.android.nash.provider.CustomerProvider

class CustomerListViewModel : CoreViewModel() {
    private val mCustomerProvider = CustomerProvider()
    private val customersLiveData = MutableLiveData<List<CustomerDataModel>>()
    private var customers = listOf<CustomerDataModel>()

    fun getCustomerLiveData() : LiveData<List<CustomerDataModel>> {
        return customersLiveData
    }

    fun getAllCustomer() {
        val disposable = mCustomerProvider.getAllCustomer().subscribe {
            customers = it
            customersLiveData.value = customers
        }
    }
}
