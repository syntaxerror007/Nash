package com.nash.android.service

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.nash.android.core.CoreViewModel
import com.nash.android.data.ServiceDataModel
import com.nash.android.data.ServiceGroupDataModel
import com.nash.android.provider.ServiceProvider
import io.reactivex.android.schedulers.AndroidSchedulers

class ServiceListViewModel : CoreViewModel() {
    private val serviceGroupListLiveData = MutableLiveData<MutableList<ServiceGroupDataModel>>()
    private val isInsertServiceGroupSuccess = MutableLiveData<Boolean>()
    private val isInsertServiceSuccess = MutableLiveData<Boolean>()
    private val insertServiceGroupError = MutableLiveData<String>()
    private val insertServiceError = MutableLiveData<String>()
    private val serviceGroupList = mutableListOf<ServiceGroupDataModel>()
    private val serviceProvider: ServiceProvider = ServiceProvider()
    private val isLoading = MutableLiveData<Boolean>()
    private val isDataChanged = MutableLiveData<Boolean>()

    fun isLoading(): LiveData<Boolean> = isLoading

    fun isDataChanged(): LiveData<Boolean> = isDataChanged

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
            val oldServiceUuid = serviceGroupDataModel.uuid
            serviceGroupDataModel.serviceGroupName = serviceGroupName
            serviceProvider.updateServiceGroup(oldServiceUuid, serviceGroupDataModel, OnCompleteListener {
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
        val disposable = ServiceProvider().getAllServiceGroup().doOnSubscribe {
            isLoading.value = true
        }.observeOn(AndroidSchedulers.mainThread())
                .flatMapIterable {
                    it
                }.flatMap {
                    ServiceProvider().getServiceFromServiceGroup(it)
                }.toList()
                .subscribe(
                        {
                            isLoading.value = false
                            serviceGroupList.clear()
                            serviceGroupList.addAll(it)
                            serviceGroupListLiveData.value = serviceGroupList
                        }
                ) {
                    isLoading.value = true
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

    fun updateService(prevServiceDataModel: ServiceDataModel?, serviceGroupDataModel: ServiceGroupDataModel?, position: Int) {
        if (prevServiceDataModel != null && serviceGroupDataModel != null) {
            serviceProvider.updateService(prevServiceDataModel)
            serviceGroupDataModel.services[position] = prevServiceDataModel
        }
    }

    fun removeServiceGroup(serviceGroupDataModel: ServiceGroupDataModel?) {
        if (serviceGroupDataModel != null)
            serviceProvider.deleteServiceGroup(serviceGroupDataModel.uuid)
                    .doOnSubscribe { isLoading.value = true }.subscribe {
                        isLoading.value = false
                        isDataChanged.value = true
                    }
    }

}