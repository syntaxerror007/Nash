package com.android.nash.customer.customerservice

import android.content.Context
import android.os.Bundle
import com.android.nash.R
import com.android.nash.core.CoreViewModel
import com.android.nash.core.dialog.CoreDialog
import com.android.nash.data.CustomerServiceDataModel
import com.android.nash.util.convertToPrice
import com.android.nash.util.convertToString
import kotlinx.android.synthetic.main.customer_service_detail_dialog.*

class CustomerServiceDetailDialog(context: Context, private val customerServiceDataModel: CustomerServiceDataModel) : CoreDialog<CoreViewModel>(context) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_service_detail_dialog)
        buttonClose.setOnClickListener { dismiss() }
        setData(customerServiceDataModel = customerServiceDataModel)
        initDialogSize()
    }


    private fun setData(customerServiceDataModel: CustomerServiceDataModel) {
        customerName.text = customerServiceDataModel.customerDataModel.customerName
        customerPhone.text = customerServiceDataModel.customerDataModel.customerPhone
        location.text = customerServiceDataModel.locationName
        serviceName.text = customerServiceDataModel.service.serviceName
        therapistName.text = customerServiceDataModel.therapist.therapistName
        treatmentDate.text = customerServiceDataModel.treatmentDate.convertToString()
        toRemindDate.text = customerServiceDataModel.toRemindDate.convertToString()
        price.text = "Rp. ${customerServiceDataModel.price.convertToPrice()}"
    }
}