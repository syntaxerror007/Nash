package com.android.nash.customer.customersearch

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import com.android.nash.core.activity.CoreActivity
import com.android.nash.R
import com.android.nash.customer.customerservice.CustomerServiceActivity
import com.android.nash.data.CustomerDataModel
import kotlinx.android.synthetic.main.customer_list_activity.*

class CustomerListActivity : CoreActivity<CustomerListViewModel>() {
    override fun onCreateViewModel(): CustomerListViewModel = ViewModelProviders.of(this).get(CustomerListViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_list_activity)
        val customerDataModel = CustomerDataModel("uuid", "Hana", "hana@gmail.com", "082299199191", listOf(), true)
        recyclerViewCustomer.adapter = CustomerListAdapter(listOf(customerDataModel, customerDataModel, customerDataModel, customerDataModel, customerDataModel, customerDataModel)) {
            startActivity(Intent(this, CustomerServiceActivity::class.java))
        }
    }
}