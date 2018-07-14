package com.android.nash.location

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.android.nash.core.CoreViewModel
import com.android.nash.data.LocationDataModel
import com.android.nash.data.UserDataModel
import com.android.nash.provider.LocationProvider
import com.android.nash.util.LOCATION_DB
import com.google.android.gms.tasks.OnCompleteListener

class RegisterLocationViewModel: CoreViewModel() {
    private val isLoading:MutableLiveData<Boolean> = MutableLiveData()
    private val locationNameError:MutableLiveData<String> = MutableLiveData()
    private val locationAddressError:MutableLiveData<String> = MutableLiveData()
    private val locationProvider:LocationProvider = LocationProvider()

    fun init() {
        isLoading.value = false
        mDatabaseReference = mFirebaseDatabase.getReference(LOCATION_DB)
    }

    fun isLoading(): LiveData<Boolean> {
        return isLoading
    }

    fun locationNameError(): LiveData<String> {
        return locationNameError
    }

    fun locationAddressError(): LiveData<String> {
        return locationAddressError
    }

    fun registerLocation(locationName:String?, locationAddress:String?) {
        isLoading.value = true
        if (isFormValid(locationName, locationAddress)) {
            val locationDataModel = LocationDataModel(locationName!!, locationAddress!!)
            locationProvider.insertLocation(locationDataModel, OnCompleteListener {
                isLoading.value = false
                if (it.isSuccessful) {

                } else {
                    
                }
            })
        }
    }

    private fun isFormValid(locationName: String?, locationAddress: String?): Boolean {
        if (locationAddress.isNullOrBlank()) {
            locationAddressError.value = "Please input Location Address"
            return false
        }

        if (locationName.isNullOrBlank()) {
            locationNameError.value = "Please input Location Name"
            return false
        }

        return true
    }
}