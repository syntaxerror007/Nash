package com.android.nash.customer.customersearch

import com.android.nash.data.CustomerDataModel

interface CustomerItemOnClickListener {
    fun onItemClick(customerDataModel: CustomerDataModel)
}