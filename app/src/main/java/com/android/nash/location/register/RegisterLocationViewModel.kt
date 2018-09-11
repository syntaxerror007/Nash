package com.android.nash.location.register

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.android.nash.core.CoreViewModel
import com.android.nash.data.LocationDataModel
import com.android.nash.data.ServiceGroupDataModel
import com.android.nash.data.TherapistDataModel
import com.android.nash.data.UserDataModel
import com.android.nash.provider.LocationProvider
import com.android.nash.provider.ServiceProvider
import com.android.nash.provider.UserProvider
import com.android.nash.util.COMPANY_NAME
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
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
    private val totalServiceSelectedLiveData: MutableLiveData<Int> = MutableLiveData()

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

    fun registerLocation(firebaseApp: FirebaseApp, locationName:String, locationAddress:String, locationPhoneNumber:String) {
        isLoading.value = true
        if (!isFormValid(locationName, locationAddress, locationPhoneNumber)) {
            isLoading.value = false
            return
        }
        registerUser(firebaseApp, locationName, locationAddress, locationPhoneNumber)
    }

    private fun registerUser(firebaseApp: FirebaseApp, locationName:String, locationAddress:String, locationPhoneNumber:String) {
        val secondaryFirebaseAuth = FirebaseAuth.getInstance(firebaseApp)
        val username = toRegisterUserDataModel.value!!.username

        secondaryFirebaseAuth.createUserWithEmailAndPassword("$username@$COMPANY_NAME.com", toRegisterUserPassword.value!!).addOnCompleteListener { it ->
            secondaryFirebaseAuth.signOut()
            if (it.isSuccessful) {
                val createdUserUid = it.result.user.uid
                val userDataModel = UserDataModel()
                userDataModel.username = username
                userDataModel.id = createdUserUid
                userDataModel.userType = "CASHIER"

                UserProvider().insertUser(userDataModel).addOnCompleteListener {
                    toRegisterUserDataModel.value = userDataModel
                    doRegisterLocation(userDataModel, locationName, locationAddress, locationPhoneNumber)
                }
            } else {
                isLoading.value = false
                var errorMessage: String? = null
                if (it.exception != null) {
                    errorMessage = when {
                        it.exception is FirebaseAuthException -> {
                            (it.exception as FirebaseAuthException).message
                        }
                        it.exception is FirebaseNetworkException -> {
                            (it.exception as FirebaseNetworkException).message
                        }
                        else -> null
                    }
                }
            }
        }
    }

    private fun doRegisterLocation(userDataModel: UserDataModel, locationName:String, locationAddress:String, locationPhoneNumber:String) {
        val locationDataModel = LocationDataModel(locationName = locationName, locationAddress = locationAddress, phoneNumber = locationPhoneNumber, totalServices = totalServiceSelectedLiveData.value!!, user = userDataModel)
            locationProvider.insertLocation(locationDataModel, serviceGroupListLiveData.value!!, availableTherapistsLiveData.value!!,  OnCompleteListener {
                isLoading.value = false
                if (it.isSuccessful) {
                    isSuccess.value = true
                } else {
                    if (it.exception != null)
                        error.value = it.exception!!.localizedMessage
                }
            })
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


    fun setSelectedServices(selectedServices: MutableList<ServiceGroupDataModel>) {
        totalServiceSelectedLiveData.value = calculateSelectedServices(selectedServices)
        serviceGroupListLiveData.value = selectedServices
    }

    private fun calculateSelectedServices(selectedServices: MutableList<ServiceGroupDataModel>): Int {
        var count = 0
        selectedServices.iterator().forEach {
            count += it.services.size
        }
        return count
    }

    fun initData(locationDataModel: LocationDataModel?) {

    }
}