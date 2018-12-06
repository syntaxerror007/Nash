package com.android.nash.location.register

import android.app.Activity
import android.app.Dialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.nash.R
import com.android.nash.core.activity.CoreActivity
import com.android.nash.data.*
import com.android.nash.service.adapter.ServiceGroupAdapter
import com.android.nash.service.adapter.ServiceItemCallback
import com.android.nash.service.dialog.list.ServiceListCallback
import com.android.nash.service.dialog.list.ServiceListDialog
import com.android.nash.service.form.ServiceCallback
import com.android.nash.therapist.TherapistListAdapter
import com.android.nash.therapist.TherapistListCallback
import com.android.nash.therapist.assignment.TherapistAssignmentCallback
import com.android.nash.therapist.assignment.TherapistAssignmentDialog
import com.android.nash.therapist.register.RegisterTherapistDialog
import com.android.nash.therapist.register.TherapistRegisterCallback
import com.android.nash.user.register.UserRegisterCallback
import com.android.nash.user.register.UserRegisterDialog
import com.google.firebase.FirebaseApp
import kotlinx.android.synthetic.main.location_register_activity.*
import org.parceler.Parcels

class RegisterLocationActivity : CoreActivity<RegisterLocationViewModel>(), UserRegisterCallback, TherapistRegisterCallback, ServiceListCallback, ServiceItemCallback, ServiceCallback, TherapistAssignmentCallback, TherapistListCallback {

    private lateinit var serviceDialog: Dialog
    private lateinit var serviceGroupAdapter: ServiceGroupAdapter
    private lateinit var therapistAssignmentDialog: Dialog
    private var firebaseApp: FirebaseApp? = null
    private var locationDataModel: LocationDataModel? = null

    override fun onCreateViewModel(): RegisterLocationViewModel {
        return ViewModelProviders.of(this).get(RegisterLocationViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationDataModel = Parcels.unwrap(intent.extras?.getParcelable("locationDataModel"))
        getViewModel().setLoading(true)
        setContentView(R.layout.location_register_activity)
        setTitle("Location")
        setToolbarRightButtonVisible(true)
        setToolbarRightButtonText("Save Configuration")
        setBackEnabled(true)
        getViewModel().getUserDataModel().observe(this, Observer {
            getViewModel().setLoading(false)
        })
        setToolbarRightOnClickListener(View.OnClickListener { onToolbarRightButtonClicked() })
        getViewModel().getAllServices()
        initData()
        observeViewModel()
        setOnClickListener()
    }

    private fun initData() {
        getViewModel().initData(locationDataModel)
        editTextLocationName.setText(locationDataModel?.locationName)
        editTextLocationAddress.setText(locationDataModel?.locationAddress)
        editTextPhoneNumber.setText(locationDataModel?.phoneNumber)
        if (locationDataModel != null) {
            locationDataModel!!.selectedServices.forEach {
                it.services.forEach { serviceDataModel ->
                    val therapistAssigned = mutableListOf<TherapistDataModel>()
                    locationDataModel!!.therapists.filter {
                        serviceDataModel.assignedTherapistSet.contains(it.uuid)
                    }.forEach {
                        therapistAssigned.add(it)
                    }
                    getViewModel().assignTherapistToService(serviceDataModel, therapistAssigned)
                }
            }
            locationDataModel!!.therapists.forEach {
                getViewModel().registerTherapist(it)
            }
            onFinishServiceClick(locationDataModel!!.selectedServices)
            onUserCreated(locationDataModel!!.user, null)
        }

        getViewModel().error().observe(this, Observer { Toast.makeText(this, it, Toast.LENGTH_LONG).show() })
    }

    private fun onToolbarRightButtonClicked() {
        val firebaseOptions = FirebaseApp.getInstance()!!.options
        firebaseApp = FirebaseApp.initializeApp(this, firebaseOptions, "secondaryFirebaseApp")
        if (locationDataModel == null) {
            getViewModel().registerLocation(firebaseApp!!, editTextLocationName.text.toString(), editTextLocationAddress.text.toString(), editTextPhoneNumber.text.toString())
        } else {
            getViewModel().updateLocation(firebaseApp!!, locationDataModel!!.uuid, editTextLocationName.text.toString(), editTextLocationAddress.text.toString(), editTextPhoneNumber.text.toString())
        }
    }

    private fun setOnClickListener() {
        btnRegisterCashier.setOnClickListener { onButtonRegisterCashierClicked() }
        btnAddTherapist.setOnClickListener { onButtonAddTherapistClicked() }
        btnAddService.setOnClickListener { onAddServiceClicked() }
    }

    private fun onAddServiceClicked() {
        val serviceListDialog = ServiceListDialog(this, this, getViewModel().getServiceGrouplist().value?.toMutableList())
        serviceListDialog.show()
    }

    private fun onButtonRegisterCashierClicked() {
        val userRegisterDialog = UserRegisterDialog(this, this)
        userRegisterDialog.show()
    }

    private fun onButtonAddTherapistClicked() {
        val registerTherapistDialog = RegisterTherapistDialog(this, this)
        registerTherapistDialog.show()
    }

    override fun onFinishServiceClick(selectedServices: MutableList<ServiceGroupDataModel>) {
        serviceGroupAdapter = ServiceGroupAdapter(selectedServices, true, true)
        serviceGroupAdapter.setServiceItemCallback(this)
        serviceGroupAdapter.expandAllGroups()
        recyclerViewService.adapter = serviceGroupAdapter
        getViewModel().setSelectedServices(selectedServices)
        serviceGroupAdapter.notifyDataSetChanged()
    }

    override fun onTherapistRegister(therapistDataModel: TherapistDataModel, position: Int) {
        if (therapistDataModel.uuid.isEmpty()) {
            getViewModel().registerTherapist(therapistDataModel)
        } else {
            getViewModel().editTherapist(therapistDataModel, position)
        }
    }

    override fun onUserCreated(userDataModel: UserDataModel, password: String?) {
        getViewModel().setUserDataModel(userDataModel)
        if (password != null)
            getViewModel().setUserPassword(password)
        btnRegisterCashier.text = userDataModel.username
    }


    private fun observeViewModel() {
        getViewModel().isLoading().observe(this, Observer { processLoading(it!!) })
        getViewModel().locationAddressError().observe(this, Observer { processLocationAddressError(it) })
        getViewModel().locationNameError().observe(this, Observer { processAddressNameError(it) })
        getViewModel().phoneNumberError().observe(this, Observer { processPhoneNumberError(it) })
        getViewModel().availableTherapistsLiveData().observe(this, Observer { processTherapistsList(it) })
        getViewModel().getTherapistAssignmentLiveData().observe(this, Observer { Toast.makeText(this, "changed", Toast.LENGTH_LONG).show() })
        getViewModel().isSuccess().observe(this, Observer {
            if (it != null && it) {
                Toast.makeText(this, "Successfully add new Location", Toast.LENGTH_LONG).show()
                val intent = Intent()
                intent.putExtra("isSuccess", true)
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {

            }
        })
    }

    override fun onItemEdit(serviceGroupDataModel: ServiceGroupDataModel, serviceDataModel: ServiceDataModel?, groupPosition: Int, childPosition: Int) {
        therapistAssignmentDialog = TherapistAssignmentDialog(this, serviceDataModel, getViewModel().availableTherapistsLiveData().value!!.filter { !it.isDeleted }, this)
        therapistAssignmentDialog.show()
    }

    override fun onEditService(prevServiceDataModel: ServiceDataModel?, serviceGroupDataModel: ServiceGroupDataModel?, groupPosition: Int, position: Int) {
        serviceGroupAdapter.serviceGroups[groupPosition] = serviceGroupDataModel!!
        getViewModel().setSelectedServices(serviceGroupAdapter.serviceGroups)
        serviceGroupAdapter.notifyDataSetChanged()
        serviceDialog.dismiss()
    }


    private fun processTherapistsList(therapists: List<TherapistDataModel>?) {
        recyclerViewTherapist.adapter = TherapistListAdapter(therapists!!.filter { !it.isDeleted }, this)
    }


    private fun processPhoneNumberError(it: String?) {
        editTextPhoneNumber.error = it
    }

    private fun processAddressNameError(it: String?) {
        editTextLocationName.error = it
    }

    private fun processLocationAddressError(it: String?) {
        editTextLocationAddress.error = it
    }

    private fun processLoading(isLoading: Boolean) {
        if (isLoading) {
            showLoadingDialog()
        } else {
            firebaseApp?.delete()
            hideLoadingDialog()
        }
    }


    override fun onTherapistEdit(therapistDataModel: TherapistDataModel, position: Int) {
        val registerTherapistDialog = RegisterTherapistDialog(this, this, therapistDataModel, position)
        registerTherapistDialog.show()
    }

    override fun onTherapistDelete(therapistDataModel: TherapistDataModel, position: Int) {
        getViewModel().deleteTherapist(therapistDataModel, position)
    }

    override fun onTherapistAssigned(serviceDataModel: ServiceDataModel?, assignedTherapist: List<TherapistDataModel>) {
        if (therapistAssignmentDialog.isShowing)
            therapistAssignmentDialog.dismiss()
        getViewModel().assignTherapistToService(serviceDataModel, assignedTherapist)
    }
}