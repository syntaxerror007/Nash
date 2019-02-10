package com.nash.android.customer.customerservice

import android.content.Context
import android.os.Bundle
import com.nash.android.R
import com.nash.android.core.CoreViewModel
import com.nash.android.core.dialog.CoreDialog
import com.nash.android.data.CustomerServiceDataModel
import com.nash.android.util.convertToPrice
import com.nash.android.util.convertToString
import kotlinx.android.synthetic.main.customer_service_detail_dialog.*

class CustomerServiceDetailDialog(context: Context, private val customerServiceDataModel: CustomerServiceDataModel) : CoreDialog<CoreViewModel>(context) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_service_detail_dialog)
        buttonClose.setOnClickListener { dismiss() }
        setData(customerServiceDataModel = customerServiceDataModel)
    }


    private fun setData(customerServiceDataModel: CustomerServiceDataModel) {
        customerName.text = customerServiceDataModel.customerDataModel?.customerName
        customerPhone.text = customerServiceDataModel.customerDataModel?.customerPhone
        location.text = customerServiceDataModel.locationName
        serviceName.text = customerServiceDataModel.service?.serviceName
        therapistName.text = customerServiceDataModel.therapist?.therapistName
        treatmentDate.text = customerServiceDataModel.treatmentDate.convertToString()
        toRemindDate.text = customerServiceDataModel.toRemindDate.convertToString()
    }
}