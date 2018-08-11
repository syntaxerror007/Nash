package com.android.nash.location

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.widget.Toast
import com.android.nash.R
import com.android.nash.core.activity.CoreActivity
import com.android.nash.data.TherapistDataModel
import com.android.nash.data.UserDataModel
import com.android.nash.therapist.RegisterTherapistDialog
import com.android.nash.therapist.TherapistListAdapter
import com.android.nash.therapist.TherapistRegisterCallback
import com.android.nash.user.register.UserRegisterCallback
import com.android.nash.user.register.UserRegisterDialog
import kotlinx.android.synthetic.main.location_register_activity.*

class RegisterLocationActivity: CoreActivity<RegisterLocationViewModel>(), UserRegisterCallback, TherapistRegisterCallback {

    override fun onCreateViewModel(): RegisterLocationViewModel {
        return ViewModelProviders.of(this).get(RegisterLocationViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.location_register_activity)
        setTitle("Location")

        observeViewModel()
        setOnClickListener()
    }

    private fun setOnClickListener() {
        btnRegisterCashier.setOnClickListener { onButtonRegisterCashierClicked() }
        btnAddTherapist.setOnClickListener { onButtonAddTherapistClicked() }
    }

    private fun onButtonRegisterCashierClicked() {
        val userRegisterDialog = UserRegisterDialog(this, this)
        userRegisterDialog.show()
    }

    private fun onButtonAddTherapistClicked() {
        val registerTherapistDialog = RegisterTherapistDialog(this, this)
        registerTherapistDialog.show()
    }


    override fun onTherapistRegister(therapistDataModel: TherapistDataModel) {
        getViewModel().registerTherapist(therapistDataModel)
    }

    override fun onUserCreated(userDataModel: UserDataModel, password: String) {
        getViewModel().setUserDataModel(userDataModel)
        getViewModel().setUserPassword(password)
        btnRegisterCashier.text = userDataModel.username
    }


    private fun observeViewModel() {
        getViewModel().isLoading().observe(this, Observer { processLoading(it!!) })
        getViewModel().locationAddressError().observe(this, Observer { processLocationAddressError(it) })
        getViewModel().locationNameError().observe(this, Observer { processAddressNameError(it) })
        getViewModel().phoneNumberError().observe(this, Observer { processPhoneNumberError(it) })
        getViewModel().availableTherapistsLiveData().observe(this, Observer { processTherapistsList(it) })
        getViewModel().isSuccess().observe(this, Observer {
            if (it != null && it) {
                Toast.makeText(this, "Successfully add new Location", Toast.LENGTH_LONG).show()
                finish()
            } else {

            }
        })
    }

    private fun processTherapistsList(therapists: List<TherapistDataModel>?) {
        recyclerViewTherapist.adapter = TherapistListAdapter(therapists!!) { onTherapistItemClicked() }
    }

    private fun onTherapistItemClicked() {

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

    private fun processLoading(it: Boolean) {

    }
}