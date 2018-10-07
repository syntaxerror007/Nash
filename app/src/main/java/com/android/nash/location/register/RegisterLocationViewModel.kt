package com.android.nash.location.register

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.android.nash.core.CoreViewModel
import com.android.nash.data.*
import com.android.nash.provider.LocationProvider
import com.android.nash.provider.ServiceProvider
import com.android.nash.provider.UserProvider
import com.android.nash.util.COMPANY_NAME
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import io.reactivex.android.schedulers.AndroidSchedulers

class RegisterLocationViewModel : CoreViewModel() {
    private val isLoading: MutableLiveData<Boolean> = MutableLiveData()
    private val isSuccess: MutableLiveData<Boolean> = MutableLiveData()
    private val locationNameError: MutableLiveData<String> = MutableLiveData()
    private val locationAddressError: MutableLiveData<String> = MutableLiveData()
    private val phoneNumberError: MutableLiveData<String> = MutableLiveData()
    private val error: MutableLiveData<String> = MutableLiveData()
    private val locationProvider: LocationProvider = LocationProvider()
    private val toRegisterUserDataModel: MutableLiveData<UserDataModel> = MutableLiveData()
    private val toRegisterUserPassword: MutableLiveData<String> = MutableLiveData()
    private val availableTherapists: MutableList<TherapistDataModel> = mutableListOf()
    private val serviceGroupList = mutableListOf<ServiceGroupDataModel>()
    private val serviceGroupListLiveData = MutableLiveData<List<ServiceGroupDataModel>>()
    private val availableTherapistsLiveData: MutableLiveData<MutableList<TherapistDataModel>> = MutableLiveData()
    private val totalServiceSelectedLiveData: MutableLiveData<Int> = MutableLiveData()
    private val therapistAssignmentMapLiveDataModel: MutableLiveData<MutableMap<String, List<TherapistDataModel>>> = MutableLiveData()
    private val therapistAssignmentMap = mutableMapOf<String, List<TherapistDataModel>>()

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

    fun getTherapistAssignmentLiveData(): LiveData<MutableMap<String, List<TherapistDataModel>>> {
        return therapistAssignmentMapLiveDataModel
    }

    fun registerLocation(firebaseApp: FirebaseApp, locationName: String, locationAddress: String, locationPhoneNumber: String) {
        isLoading.value = true
        if (!isFormValid(locationName, locationAddress, locationPhoneNumber)) {
            isLoading.value = false
            return
        }
        registerUser(firebaseApp, OnCompleteListener {
            doRegisterLocation(locationName, locationAddress, locationPhoneNumber, OnCompleteListener { registerLocation ->
                isLoading.value = false
                if (registerLocation.isSuccessful) {
                    isSuccess.value = true
                } else {
                    if (registerLocation.exception != null)
                        error.value = registerLocation.exception!!.localizedMessage
                }
            })
        })
    }

    private fun registerUser(firebaseApp: FirebaseApp, onCompleteListener: OnCompleteListener<Void>) {
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
                    onCompleteListener.onComplete(it)
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

    private fun doRegisterLocation(locationName: String, locationAddress: String, locationPhoneNumber: String, onCompleteListener: OnCompleteListener<Task<Void>>) {
        val locationDataModel = LocationDataModel(locationName = locationName, locationAddress = locationAddress, phoneNumber = locationPhoneNumber, user = toRegisterUserDataModel.value!!, totalServices = totalServiceSelectedLiveData.value!!)
        locationProvider.insertLocation(locationDataModel, serviceGroupListLiveData.value!!, availableTherapistsLiveData.value!!, therapistAssignmentMapLiveDataModel.value!!, onCompleteListener)
    }

    private fun doUpdateLocation(locationName: String, locationAddress: String, locationPhoneNumber: String, onCompleteListener: OnCompleteListener<Task<Void>>) {
        val locationDataModel = LocationDataModel(locationName = locationName, locationAddress = locationAddress, phoneNumber = locationPhoneNumber, user = toRegisterUserDataModel.value!!, totalServices = totalServiceSelectedLiveData.value!!)
        locationProvider.updateLocation(locationDataModel, serviceGroupListLiveData.value!!, availableTherapistsLiveData.value!!, therapistAssignmentMapLiveDataModel.value!!, onCompleteListener)
    }

    fun getAllServices() {
        val disposable = ServiceProvider().getAllServiceGroup().observeOn(AndroidSchedulers.mainThread())
                .flatMapIterable {
                    it
                }.flatMap {
                    ServiceProvider().getServiceFromServiceGroup(it)
                }.toList()
                .subscribe({
                    serviceGroupList.clear()
                    serviceGroupList.addAll(it)
                    serviceGroupListLiveData.value = serviceGroupList
                }) {
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
        if (locationDataModel != null) {
            toRegisterUserDataModel.value = locationDataModel.user
            availableTherapistsLiveData.value = locationDataModel.therapists
            serviceGroupListLiveData.value = locationDataModel.selectedServices
            totalServiceSelectedLiveData.value = locationDataModel.selectedServices.size
        }
    }

    fun updateLocation(firebaseApp: FirebaseApp, locationUUID: String, locationName: String, locationAddress: String, locationPhoneNumber: String) {
        isLoading.value = true
        if (!isFormValid(locationName, locationAddress, locationPhoneNumber)) {
            isLoading.value = false
            return
        }
        if (toRegisterUserPassword.value != null) {
            registerUser(firebaseApp, OnCompleteListener {
                doUpdateLocation(locationUUID, locationName, locationAddress, locationPhoneNumber)
            })
        } else {
            doUpdateLocation(locationUUID, locationName, locationAddress, locationPhoneNumber)
        }
    }

    private fun doUpdateLocation(locationUUID: String, locationName: String, locationAddress: String, locationPhoneNumber: String) {
        doUpdateLocation(locationName, locationAddress, locationPhoneNumber, OnCompleteListener { it ->
            if (it.isSuccessful) {
                locationProvider.removeLocation(locationUUID, OnCompleteListener { it2 ->
                    if (it2.isSuccessful) {
                        isLoading.value = false
                        isSuccess.value = true
                    }
                })
            }
        })
    }

    fun assignTherapistToService(serviceDataModel: ServiceDataModel?, assignedTherapists: List<TherapistDataModel>) {
        if (serviceDataModel != null) {
            therapistAssignmentMap[serviceDataModel.uuid] = assignedTherapists
            therapistAssignmentMapLiveDataModel.value = therapistAssignmentMap
            serviceDataModel.numTherapist = assignedTherapists.size
            assignedTherapists.forEach {assignedTherapist ->
                availableTherapists.forEach {availableTherapist ->
                    if (availableTherapist.therapistName == assignedTherapist.therapistName) {
                        availableTherapist.assignmentSet.add(serviceDataModel.uuid)
                        availableTherapist.job = availableTherapist.assignmentSet.size
                    }
                }
            }

            availableTherapistsLiveData.value = availableTherapists
            serviceGroupListLiveData.value = serviceGroupList
        }
    }

    fun setLoading(isLoading: Boolean) {
        this.isLoading.value = isLoading
    }
}