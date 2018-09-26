package com.android.nash.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import com.android.nash.R
import com.android.nash.core.activity.CoreActivity
import com.android.nash.customer.customersearch.CustomerListActivity
import com.android.nash.customer.customerservice.CustomerServiceActivity
import com.android.nash.customer.newcustomer.CustomerNewFormActivity
import com.android.nash.data.UserDataModel
import com.android.nash.location.list.LocationListActivity
import kotlinx.android.synthetic.main.home_activity.*

class HomeActivity : CoreActivity<HomeViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        getViewModel().getUserDataModel().observe(this, Observer { onUserLoaded(it!!) })
        hideToolbar()
        layoutSearch.setOnClickListener { gotoSearchActivity() }
        layoutNewCustomer.setOnClickListener { gotoNewCustomer() }
        layoutReminder.setOnClickListener { gotoReminder() }
    }

    private fun onUserLoaded(userDataModel: UserDataModel) {
        if (userDataModel.userType == "ADMIN") {
            startActivity(Intent(this, LocationListActivity::class.java))
            finish()
        }
    }

    private fun gotoReminder() {

    }

    private fun gotoNewCustomer() {
        startActivity(Intent(this, CustomerNewFormActivity::class.java))
    }

    private fun gotoSearchActivity() {
        startActivity(Intent(this, CustomerListActivity::class.java))
    }

    override fun onCreateViewModel(): HomeViewModel {
        return ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }
}