package com.android.nash.reminder

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.android.nash.R
import com.android.nash.core.activity.CoreActivity
import com.android.nash.data.CustomerServiceDataModel
import kotlinx.android.synthetic.main.customer_service_activity.*

class CustomerReminderActivity : CoreActivity<CustomerReminderViewModel>() {
    override fun onCreateViewModel() = ViewModelProviders.of(this).get(CustomerReminderViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_reminder_activity)
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