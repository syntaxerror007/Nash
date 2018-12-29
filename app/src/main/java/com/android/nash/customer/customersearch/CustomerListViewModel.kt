package com.android.nash.customer.customersearch

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.android.nash.core.CoreViewModel
import com.android.nash.data.CustomerDataModel
import com.android.nash.provider.CustomerProvider
import de.siegmar.fastcsv.writer.CsvAppender
import de.siegmar.fastcsv.writer.CsvWriter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

class CustomerListViewModel : CoreViewModel() {
    private val mCustomerProvider = CustomerProvider()
    private val customersLiveData = MutableLiveData<List<CustomerDataModel>>()
    private var customers = mutableListOf<CustomerDataModel>()

    private val isLoadingLiveData = MutableLiveData<Boolean>()
    private val isLoadMore = MutableLiveData<Boolean>()
    private val isLoadFinish = MutableLiveData<Boolean>()

    private val lastLoadedItem = MutableLiveData<String>()

    fun isLoadingLiveData(): LiveData<Boolean> = isLoadingLiveData

    fun getCustomerLiveData(): LiveData<List<CustomerDataModel>> = customersLiveData

    fun getAllCustomer() {
        isLoadingLiveData.value = true
        val disposable = mCustomerProvider.getAllCustomer().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
            isLoadingLiveData.value = false
            isLoadMore.value = false
            customers = it.toMutableList()
            customersLiveData.value = customers
            lastLoadedItem.value = it.last().uuid
        }
    }

    fun listenOnTextChanged() {
        val disposable = autoCompletePublishSubject
                .debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .switchMap {
                    if (it.isEmpty()) {
                        mCustomerProvider.getAllCustomer()
                    } else {
                        mCustomerProvider.searchCustomer(it)
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (result.isNotEmpty()) {
                        isLoadFinish.value = false
                        customers = result.toMutableList()
                        customersLiveData.value = customers
                        lastLoadedItem.value = result.last().uuid
                    } else {
                        isLoadFinish.value = true
                    }
                }, { })
    }

    fun loadMore() {
        if ((isLoadFinish.value == null || isLoadFinish.value!!.not()) && isLoadMore.value!!.not()) {
            isLoadMore.value = true
            val disposable = mCustomerProvider.getAllCustomer(lastLoadedItem.value).filter { t: List<CustomerDataModel> -> t.isNotEmpty() }.subscribe {
                if (it.isNotEmpty()) {
                    isLoadMore.value = false
                    customers.addAll(it)
                    customersLiveData.value = customers
                    lastLoadedItem.value = it.last().uuid
                } else {
                    isLoadFinish.value = true
                }
            }
        }
    }

    fun downloadCustomer(file: File) {
        var csvWriter = CsvWriter()
        var appender = csvWriter.append(file, StandardCharsets.UTF_8)
        writeLine(CustomerDataModel.getCsvHeader(), appender)
        isLoadingLiveData.value = true
        var temp = mCustomerProvider.getAllCustomerKey()
                .concatMapIterable { it }
                .concatMapMaybe {
                    mCustomerProvider.getCustomerFromUUID(it)
                }.concatMap {
                    try {
                        val rowData = it.toCsvRow()
                        writeLine(rowData, appender)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                    }
                    return@concatMap Observable.just(it)
                }.subscribe({
                    isLoadingLiveData.value = false
                }) {
                    it.printStackTrace()
                }
    }

    private fun writeLine(temps: Array<String>, appender: CsvAppender) {
        appender.apply {
            temps.forEach { appendField(it) }
            endLine()
            flush()
        }
    }
}
