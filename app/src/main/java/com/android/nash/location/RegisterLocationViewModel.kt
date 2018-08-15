package com.android.nash.location

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.android.nash.core.CoreViewModel
import com.android.nash.data.LocationDataModel
import com.android.nash.data.ServiceGroupDataModel
import com.android.nash.data.TherapistDataModel
import com.android.nash.data.UserDataModel
import com.android.nash.provider.LocationProvider
import com.android.nash.provider.ServiceProvider
import com.android.nash.util.LOCATION_DB
import com.google.android.gms.tasks.OnCompleteListener
import io.reactivex.android.schedulers.AndroidSchedulers

class RegisterLocationViewModel: CoreViewModel() {
    private val isLoading:MutableLiveData<Boolean> = MutableLiveData()
    private val isSuccess:MutableLiveData<Boolean> = MutableLiveData()
    private val locationNameError:MutableLiveData<String> = MutableLiveData()
    private val locationAddressError:MutableLiveData<String> = MutableLiveData()
    private val phoneNumberError:MutableLiveData<String> = MutableLiveData()
    private val error:MutableLiveData<String> = MutableLiveData()
    private val locationProvider:LocationProvider = LocationProvider()
    private val toRegisterUserDataModel:MutableLiveData<UserDataModel> = MutableLiveData()
    private val toRegisterUserPassword: MutableLiveData<String> = MutableLiveData()
    private val availableTherapists:MutableList<TherapistDataModel> = mutableListOf()
    private val serviceGroupList = mutableListOf<ServiceGroupDataModel>()
    private val serviceGroupListLiveData = MutableLiveData<List<ServiceGroupDataModel>>()
    private val availableTherapistsLiveData:MutableLiveData<MutableList<TherapistDataModel>> = MutableLiveData()

    fun isLoading(): LiveData<Boolean> {
        return isLoading
    }

    fun locationNameError(): LiveData<String> {
        return locationNameError
    }

    fun locationAddressError(): LiveData<String> {
        return locationAddressError
    }

    fun isSuccess(): LiveData<Boolean> {
        return isSuccess
    }

    fun availableTherapistsLiveData(): LiveData<MutableList<TherapistDataModel>> {
        return availableTherapistsLiveData
    }

    fun getServiceGrouplist(): LiveData<List<ServiceGroupDataModel>> {
        return serviceGroupListLiveData
    }

    fun registerLocation(locationName:String, locationAddress:String, locationPhoneNumber:String) {
        isLoading.value = true
        if (isFormValid(locationName, locationAddress, locationPhoneNumber)) {
            val locationDataModel = LocationDataModel(locationName, locationAddress, locationPhoneNumber)
            locationProvider.insertLocation(locationDataModel, OnCompleteListener {
                isLoading.value = false
                if (it.isSuccessful) {
                    isSuccess.value = true
                } else {
                    if (it.exception != null)
                        error.value = it.exception!!.localizedMessage
                }
            })
        }
    }

    fun getAllServices() {
        ServiceProvider().getAllServiceGroup().observeOn(AndroidSchedulers.mainThread())
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

    private fun isFormValid(locationName: String?, locationAddress: String?, phoneNumber: String?): Boolean {
        if (locationAddress.isNullOrBlank()) {
            locationAddressError.value = "Please input Location Address"
            return false
        }

        if (locationName.isNullOrBlank()) {
            locationNameError.value = "Please input Location Name"
            return false
        }

        if (phoneNumber.isNullOrBlank()) {
            phoneNumberError.value = "Please input Phone Number"
            return false
        }

        return true
    }

    fun phoneNumberError(): LiveData<String> {
        return phoneNumberError
    }

    fun setUserDataModel(userDataModel: UserDataModel) {
        toRegisterUserDataModel.value = userDataModel
    }

    fun setUserPassword(password: String) {
        toRegisterUserPassword.value = password
    }

    fun registerTherapist(therapistDataModel: TherapistDataModel) {
        availableTherapists.add(therapistDataModel)
        availableTherapistsLiveData.value = availableTherapists
    }
}