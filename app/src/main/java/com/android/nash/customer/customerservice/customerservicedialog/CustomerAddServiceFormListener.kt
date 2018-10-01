package com.android.nash.customer.customerservice.customerservicedialog

import com.android.nash.data.CustomerServiceDataModel

interface CustomerAddServiceFormListener {
    fun onServiceSelected(serviceUUID: String)
    fun onSubmit(customerServiceDataModel: CustomerServiceDataModel)
    fun onCancel()
}
