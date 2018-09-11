package com.android.nash.service

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.android.nash.core.CoreViewModel
import com.android.nash.data.ServiceDataModel
import com.android.nash.data.ServiceGroupDataModel
import com.android.nash.provider.ServiceProvider
import com.google.android.gms.tasks.OnCompleteListener
import io.reactivex.android.schedulers.AndroidSchedulers

class ServiceListViewModel : CoreViewModel() {
    private val serviceGroupListLiveData = MutableLiveData<MutableList<ServiceGroupDataModel>>()
    private val isInsertServiceGroupSuccess = MutableLiveData<Boolean>()
    private val isInsertServiceSuccess = MutableLiveData<Boolean>()
    private val insertServiceGroupError = MutableLiveData<String>()
    private val insertServiceError = MutableLiveData<String>()
    private val serviceGroupList = mutableListOf<ServiceGroupDataModel>()
    private val serviceProvider:ServiceProvider = ServiceProvider()

    fun getServiceGroupListLiveData(): LiveData<MutableList<ServiceGroupDataModel>> {
        return serviceGroupListLiveData
    }

    fun isInsertServiceGroupSuccess(): LiveData<Boolean> {
        return isInsertServiceGroupSuccess
    }

    fun getInsertServiceGroupError(): LiveData<String> {
        return insertServiceGroupError
    }

    fun isInsertServiceSuccess(): LiveData<Boolean> {
        return isInsertServiceSuccess
    }

    fun getInsertServiceError(): LiveData<String> {
        return insertServiceError
    }

    fun insertServiceGroup(serviceGroupDataModel: ServiceGroupDataModel?, serviceGroupName: String) {
        if (serviceGroupDataModel != null) {
            val oldServiceGroupName = serviceGroupDataModel.serviceGroupName
            serviceGroupDataModel.serviceGroupName = serviceGroupName
            serviceProvider.updateServiceGroup(oldServiceGroupName, serviceGroupDataModel, OnCompleteListener {
                if (it.isSuccessful) {
                    loadAllService()
                    isInsertServiceGroupSuccess.value = true
                } else {
                    isInsertServiceGroupSuccess.value = false
                    if (it.exception != null)
                        insertServiceGroupError.value = it.exception!!.localizedMessage
                }
            })
        } else {
            val serviceGroup = ServiceGroupDataModel(serviceGroupName = serviceGroupName, uuid = "", services = mutableListOf())
            serviceProvider.insertServiceGroup(serviceGroup, OnCompleteListener {
                if (it.isSuccessful) {
                    loadAllService()
                    isInsertServiceGroupSuccess.value = true
                } else {
                    isInsertServiceGroupSuccess.value = false
                    if (it.exception != null)
                        insertServiceGroupError.value = it.exception!!.localizedMessage
                }
            })
        }
    }

    fun loadAllService() {
        val disposable = serviceProvider.getAllServiceGroup().observeOn(AndroidSchedulers.mainThread())
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
        serviceProvider.insertService(serviceGroupDataModel, serviceDataModel, OnCompleteListener {
            if (it.isSuccessful) {
                loadAllService()
                isInsertServiceSuccess.value = true
            } else {
                isInsertServiceSuccess.value = false
                insertServiceError.value = it.exception?.localizedMessage
            }
        })
    }

    fun removeService(serviceGroupDataModel: ServiceGroupDataModel, serviceDataModel: ServiceDataModel?) {
        serviceProvider.deleteService(serviceGroupDataModel, serviceDataModel, OnCompleteListener {
            if (it.isSuccessful) {
                loadAllService()
            }
        })
    }

}