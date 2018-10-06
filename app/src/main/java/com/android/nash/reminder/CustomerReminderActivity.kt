package com.android.nash.reminder

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.android.nash.R
import com.android.nash.core.activity.CoreActivity

class CustomerReminderActivity : CoreActivity<CustomerReminderViewModel>() {
    override fun onCreateViewModel() = ViewModelProviders.of(this).get(CustomerReminderViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_service_activity)
        observeViewModel()
    }

    private fun observeViewModel() {
        getViewModel().getUserDataModel().observe(this, Observer {
            if (it != null) {
                getViewModel().getToRemindCustomer(System.currentTimeMillis())
            }
        })

    }
}