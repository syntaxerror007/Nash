package com.android.nash.service.form.service

import android.content.Context
import android.os.Bundle
import com.android.nash.R
import com.android.nash.core.dialog.CoreDialog
import com.android.nash.data.ServiceDataModel
import com.android.nash.data.ServiceGroupDataModel
import com.android.nash.service.form.ServiceCallback
import kotlinx.android.synthetic.main.service_form_dialog.*

class ServiceFormDialog(context: Context, serviceCallback: ServiceCallback, serviceGroupDataModel: ServiceGroupDataModel?, position:Int) : CoreDialog<ServiceFormViewModel>(context) {
    private var serviceCallback: ServiceCallback = serviceCallback
    private var serviceGroupDataModel: ServiceGroupDataModel? = serviceGroupDataModel
    private var position: Int = position

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.service_form_dialog)
        initDialogSize()
        setListener()
    }

    private fun setListener() {
        buttonClose.setOnClickListener { dismiss() }
        btnCancel.setOnClickListener { dismiss() }
        btnRegister.setOnClickListener { registerServiceToServiceGroup() }
    }

    private fun registerServiceToServiceGroup() {
        val serviceName = editTextServiceName.text.toString()
        val price = editTextPrice.text.toString()
        val reminder = editTextReminder.text.toString()
        val serviceDataModel = ServiceDataModel(serviceName = serviceName, price = price.toLong(), reminder = reminder.toInt(), id = "", shouldFreeText = false)
        serviceCallback.onServiceCreated(serviceDataModel = serviceDataModel, serviceGroupDataModel = serviceGroupDataModel, position = position)
    }
}