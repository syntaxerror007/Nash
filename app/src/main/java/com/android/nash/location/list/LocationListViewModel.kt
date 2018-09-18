package com.android.nash.location.list

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.android.nash.core.CoreViewModel
import com.android.nash.data.LocationDataModel
import com.android.nash.provider.LocationProvider
import com.google.android.gms.tasks.OnCompleteListener
import io.reactivex.android.schedulers.AndroidSchedulers

class LocationListViewModel : CoreViewModel() {
    private val locationListLiveData: MutableLiveData<List<LocationDataModel>> = MutableLiveData()
    private val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun getLocationListData() : LiveData<List<LocationDataModel>> {
        return locationListLiveData
    }

    fun getLoadingLiveData() : LiveData<Boolean> {
        return loadingLiveData
    }

    fun getAllLocation() {
        loadingLiveData.value = true
        val disposable = LocationProvider().getAllLocation().observeOn(AndroidSchedulers.mainThread()).subscribe ({
            loadingLiveData.value = false
            locationListLiveData.value = it
        }) {
            loadingLiveData.value = false
            it.printStackTrace()
        }
    }

    fun deleteLocation(it: LocationDataModel) {
        loadingLiveData.value = true
        LocationProvider().removeLocation(it.uuid, OnCompleteListener {
            loadingLiveData.value = false
            if (it.isSuccessful) {
                getAllLocation()
            }
        })
    }

}