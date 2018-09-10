package com.android.nash.service

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.android.nash.core.CoreViewModel
import com.android.nash.data.ServiceDataModel
import com.android.nash.data.ServiceGroupDataModel
import com.android.nash.provider.ServiceProvider
import com.android.nash.service.form.data.ServiceGroupModel
import com.android.nash.service.form.data.ServiceModel
import com.google.android.gms.tasks.OnCompleteListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

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

}