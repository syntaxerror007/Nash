package com.android.nash.home

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import com.android.nash.R
import com.android.nash.core.activity.CoreActivity
import com.android.nash.newcustomer.CustomerNewFormActivity
import kotlinx.android.synthetic.main.home_activity.*

class HomeActivity : CoreActivity<HomeViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        hideToolbar()
        layoutSearch.setOnClickListener { gotoSearchActivity() }
        layoutNewCustomer.setOnClickListener { gotoNewCustomer() }
        layoutReminder.setOnClickListener { gotoReminder() }
    }

    private fun gotoReminder() {

    }

    private fun gotoNewCustomer() {
        startActivity(Intent(this, CustomerNewFormActivity::class.java))
    }

    private fun gotoSearchActivity() {

    }

    override fun onCreateViewModel(): HomeViewModel {
        return ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }
}