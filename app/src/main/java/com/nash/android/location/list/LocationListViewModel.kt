package com.nash.android.location.list

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.nash.android.core.CoreViewModel
import com.nash.android.data.LocationDataModel
import com.nash.android.provider.LocationProvider
import io.reactivex.android.schedulers.AndroidSchedulers

class LocationListViewModel : CoreViewModel() {
    private val locationListLiveData: MutableLiveData<List<LocationDataModel>> = MutableLiveData()
    private val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun getLocationListData(): LiveData<List<LocationDataModel>> {
        return locationListLiveData
    }

    fun getLoadingLiveData(): LiveData<Boolean> {
        return loadingLiveData
    }

    fun getAllLocation() {
        loadingLiveData.value = true
        val disposable = LocationProvider().getAllLocation().observeOn(AndroidSchedulers.mainThread()).subscribe({
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