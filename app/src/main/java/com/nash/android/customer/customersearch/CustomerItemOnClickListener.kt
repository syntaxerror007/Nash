package com.nash.android.customer.customersearch

import com.nash.android.data.CustomerDataModel

interface CustomerItemOnClickListener {
    fun onItemClick(customerDataModel: CustomerDataModel)
}