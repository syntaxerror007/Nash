package com.android.nash.service

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.android.nash.core.CoreViewModel
import com.android.nash.data.ServiceDataModel
import com.android.nash.data.ServiceGroupDataModel
import com.android.nash.provider.ServiceProvider
import com.google.android.gms.tasks.OnCompleteListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

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

    fun insertServiceGroup(serviceGroupName: String) {
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

    fun loadAllService() {
        serviceProvider.getAllServiceGroup().observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        serviceGroupList.clear()
                        serviceGroupList.addAll(it)
                        serviceGroupListLiveData.value = serviceGroupList
                    }
                ) {
                    it.printStackTrace()
                }
    }


    fun insertService(serviceGroupDataModel: ServiceGroupDataModel?, serviceDataModel: ServiceDataModel) {
        if (serviceGroupDataModel != null) {
            serviceGroupDataModel.services.add(serviceDataModel)
        }
    }

}