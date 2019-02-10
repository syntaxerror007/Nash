package com.nash.android.reminder

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.nash.android.R
import com.nash.android.core.activity.CoreActivity
import com.nash.android.data.CustomerServiceDataModel
import kotlinx.android.synthetic.main.customer_service_activity.*

class CustomerReminderActivity : CoreActivity<CustomerReminderViewModel>() {
    override fun onCreateViewModel() = ViewModelProviders.of(this).get(CustomerReminderViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_reminder_activity)

        setTitle("Reminder")
        observeViewModel()
    }

    private fun observeViewModel() {
        getViewModel().getUserDataModel().observe(this, Observer {
            if (it != null) {
                getViewModel().getToRemindCustomer(System.currentTimeMillis())
            }
        })


        getViewModel().getCustomerServiceLiveData().observe(this, Observer {
            if (it != null)
                observeCustomerService(it)
        })

        getViewModel().isLoading().observe(this, Observer {
            if (it != null && it) {
                showLoadingDialog()
            } else {
                hideLoadingDialog()
            }
        })

        getViewModel().isDataChanged().observe(this, Observer {
            if (it != null && it) {
                getViewModel().getToRemindCustomer(System.currentTimeMillis())
            }
        })
    }


    private fun observeCustomerService(it: List<CustomerServiceDataModel>) {
        recyclerViewService.adapter = CustomerReminderAdapter(it) { customerServiceDataModel, isChecked ->
            getViewModel().setCustomerHasReminded(customerServiceDataModel, isChecked)
        }
    }
}