package com.android.nash.customer.customersearch

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.android.nash.core.CoreViewModel
import com.android.nash.data.CustomerDataModel
import com.android.nash.provider.CustomerProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

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

    fun listenOnTextChanged() {
        val disposable = autoCompletePublishSubject
                .debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .switchMap { mCustomerProvider.searchCustomer(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    customers = result
                    customersLiveData.value = customers
                }, { _: Throwable -> })
    }
}
