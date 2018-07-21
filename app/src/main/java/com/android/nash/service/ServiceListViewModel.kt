package com.android.nash.service

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.android.nash.core.CoreViewModel
import com.android.nash.data.ServiceGroupDataModel
import com.android.nash.provider.ServiceProvider
import com.google.android.gms.tasks.OnCompleteListener

class ServiceListViewModel : CoreViewModel() {
    private val serviceGroupListLiveData = MutableLiveData<List<ServiceGroupDataModel>>()
    private val isInsertServiceGroupSuccess = MutableLiveData<Boolean>()
    private val insertServiceGroupError = MutableLiveData<String>()
    private val serviceGroupList = mutableListOf<ServiceGroupDataModel>()
    private val serviceProvider:ServiceProvider = ServiceProvider()

    fun getServiceGroupListLiveData(): LiveData<List<ServiceGroupDataModel>> {
        return serviceGroupListLiveData
    }

    fun isInsertServiceGroupSuccess(): LiveData<Boolean> {
        return isInsertServiceGroupSuccess
    }

    fun getInsertServiceGroupError(): LiveData<String> {
        return insertServiceGroupError
    }

    fun insertServiceDialog(serviceGroupName: String) {
        val serviceGroup = ServiceGroupDataModel(serviceGroupName = serviceGroupName, uuid = "", services = mutableListOf())
        serviceProvider.insertServiceGroup(serviceGroup, OnCompleteListener {
            if (it.isSuccessful) {
                serviceGroupList.add(serviceGroup)
                serviceGroupListLiveData.value = serviceGroupList
                isInsertServiceGroupSuccess.value = true
            } else {
                isInsertServiceGroupSuccess.value = false
                if (it.exception != null)
                    insertServiceGroupError.value = it.exception!!.localizedMessage
            }
        })
    }
}