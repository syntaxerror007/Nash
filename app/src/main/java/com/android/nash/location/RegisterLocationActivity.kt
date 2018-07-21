package com.android.nash.location

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.nash.R
import com.android.nash.core.activity.CoreActivity
import kotlinx.android.synthetic.main.location_register_activity.*

class RegisterLocationActivity: CoreActivity<RegisterLocationViewModel>() {
    override fun onCreateViewModel(): RegisterLocationViewModel {
        return ViewModelProviders.of(this).get(RegisterLocationViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.location_register_activity)
        getViewModel().isLoading().observe(this, Observer { processLoading(it!!) })
        getViewModel().locationAddressError().observe(this, Observer { processLocationAddressError(it) })
        getViewModel().locationNameError().observe(this, Observer { processAddressNameError(it) })
        getViewModel().phoneNumberError().observe(this, Observer { processPhoneNumberError(it) })
        getViewModel().isSuccess().observe(this, Observer {
            if (it != null && it) {
                Toast.makeText(this, "Successfully add new Location", Toast.LENGTH_LONG).show()
                finish()
            } else {

            }
        })
        btnRegister.setOnClickListener {
            getViewModel().registerLocation(editTextLocationName.text.toString(), editTextLocationAddress.text.toString(), editTextPhoneNumber.text.toString())
        }
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
        loading.isIndeterminate = true
        if (it) {
            loading.visibility = View.VISIBLE
            btnRegister.visibility = View.GONE
        } else {
            loading.visibility = View.GONE
            btnRegister.visibility = View.VISIBLE
        }
    }
}