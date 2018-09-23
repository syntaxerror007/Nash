package com.android.nash.customer.customerservice

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.android.nash.core.activity.CoreActivity
import com.android.nash.R
import com.android.nash.data.CustomerServiceDataModel
import com.android.nash.data.ServiceDataModel
import com.android.nash.data.TherapistDataModel
import kotlinx.android.synthetic.main.customer_service_activity.*

class CustomerServiceActivity : CoreActivity<CustomerServiceViewModel>() {
    override fun onCreateViewModel(): CustomerServiceViewModel = ViewModelProviders.of(this).get(CustomerServiceViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_service_activity)
        val serviceDataModel = ServiceDataModel(serviceName = "Service Name")
        val therapistDataModel = TherapistDataModel(therapistName =  "Therapist Name")

        val customerServiceDataModel = CustomerServiceDataModel(service = serviceDataModel, therapist = therapistDataModel, price = 100000, hasReminded = true)
        val customerServiceAdapter = CustomerServiceAdapter(listOf(customerServiceDataModel, customerServiceDataModel, customerServiceDataModel, customerServiceDataModel))
        recyclerViewService.adapter = customerServiceAdapter
    }
}