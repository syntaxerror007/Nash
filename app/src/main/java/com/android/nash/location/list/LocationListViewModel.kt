package com.android.nash.location.list

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.android.nash.core.CoreViewModel
import com.android.nash.data.LocationDataModel
import com.android.nash.provider.LocationProvider
import io.reactivex.android.schedulers.AndroidSchedulers

class LocationListViewModel : CoreViewModel() {
    private val locationListLiveData: MutableLiveData<List<LocationDataModel>> = MutableLiveData()

    fun getLocationListData() : LiveData<List<LocationDataModel>> {
        return locationListLiveData
    }

    fun getAllLocation() {
        LocationProvider().getAllLocation().observeOn(AndroidSchedulers.mainThread()).subscribe ({
            locationListLiveData.value = it
        }) {
            it.printStackTrace()
        }
    }

}