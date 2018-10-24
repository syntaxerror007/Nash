package com.android.nash.customer.newcustomer

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.android.nash.core.CoreViewModel
import com.android.nash.data.CustomerDataModel
import com.android.nash.provider.CustomerProvider
import io.reactivex.observers.DisposableCompletableObserver

class CustomerNewFormViewModel : CoreViewModel() {
    private val customerProvider = CustomerProvider()
    private val isLoading = MutableLiveData<Boolean>()
    private val isSuccess = MutableLiveData<Boolean>()
    private val errorMessage = MutableLiveData<String>()
    private var customerDataModelLiveData: CustomerDataModel? = null


    fun isLoading() : LiveData<Boolean> {
        return isLoading
    }

    fun isSuccess() : LiveData<Boolean> {
        return isSuccess
    }

    fun getErrorMessage(): LiveData<String> {
        return errorMessage
    }

    fun getCustomerLiveData(): CustomerDataModel? = customerDataModelLiveData


    fun insertCustomer(customerDataModel: CustomerDataModel) {
        isLoading.value = true
        val disposable = customerProvider.insertCustomer(customerDataModel).doOnComplete {
            isLoading.value = false
        }.subscribeWith(object: DisposableCompletableObserver() {
            override fun onComplete() {
                isSuccess.value = true
            }
            override fun onError(e: Throwable) {
                isSuccess.value = false
                errorMessage.value = e.localizedMessage.toString()
            }
        })
    }


    fun setCustomerDataModel(customerDataModel: CustomerDataModel) {
        customerDataModelLiveData = customerDataModel
    }
}