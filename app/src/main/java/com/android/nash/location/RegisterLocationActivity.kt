package com.android.nash.location

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.android.nash.core.CoreActivity

class RegisterLocationActivity: CoreActivity<RegisterLocationViewModel>() {
    override fun onCreateViewModel(): RegisterLocationViewModel {
        return ViewModelProviders.of(this).get(RegisterLocationViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getViewModel().registerLocation("Location Name", "Address")
    }
}