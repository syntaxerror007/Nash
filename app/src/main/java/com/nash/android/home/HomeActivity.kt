package com.nash.android.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import com.nash.android.R
import com.nash.android.core.activity.CoreActivity
import com.nash.android.customer.customersearch.CustomerListActivity
import com.nash.android.customer.newcustomer.CustomerNewFormActivity
import com.nash.android.data.UserDataModel
import com.nash.android.reminder.CustomerReminderActivity
import com.nash.android.util.setVisible
import kotlinx.android.synthetic.main.home_activity.*

class HomeActivity : CoreActivity<HomeViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        homeContainer.setVisible(false)
        getViewModel().setLoading(true)
        getViewModel().getUserDataModel().observe(this, Observer { onUserLoaded(it!!) })
        hideToolbar()
        layoutSearch.setOnClickListener { gotoSearchActivity() }
        layoutNewCustomer.setOnClickListener { gotoNewCustomer() }
        layoutReminder.setOnClickListener { gotoReminder() }
        btnLogout.setOnClickListener { doLogout() }
    }

    private fun onUserLoaded(userDataModel: UserDataModel) {
        getViewModel().setLoading(false)
        if (userDataModel.userType == "ADMIN") {
            startActivity(Intent(this, CustomerListActivity::class.java))
            finish()
        } else {
            homeContainer.setVisible(true)
        }
    }

    private fun gotoReminder() {
        startActivity(Intent(this, CustomerReminderActivity::class.java))
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