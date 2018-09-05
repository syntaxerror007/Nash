package com.android.nash.service.form.service

import android.content.Context
import android.os.Bundle
import com.android.nash.R
import com.android.nash.core.dialog.CoreDialog
import com.android.nash.data.ServiceDataModel
import com.android.nash.data.ServiceGroupDataModel
import com.android.nash.service.form.ServiceCallback
import kotlinx.android.synthetic.main.service_form_dialog.*

class ServiceFormDialog(context: Context, serviceCallback: ServiceCallback, serviceGroupDataModel: ServiceGroupDataModel?, groupPosition:Int, position:Int,  isEditPriceOnly: Boolean = false, serviceDataModel: ServiceDataModel? = null) : CoreDialog<ServiceFormViewModel>(context) {
    private var serviceCallback: ServiceCallback = serviceCallback
    private var serviceGroupDataModel: ServiceGroupDataModel? = serviceGroupDataModel
    private var groupPosition: Int = groupPosition
    private var position:Int = position
    private var isEditPriceOnly: Boolean = isEditPriceOnly
    private var prevServiceDataModel = serviceDataModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.service_form_dialog)
        if (isEditPriceOnly) {
            editTextPrice.setText("${prevServiceDataModel?.price}")
            editTextServiceName.setText(prevServiceDataModel?.serviceName)
            editTextReminder.setText("${prevServiceDataModel?.reminder}")
            editTextServiceName.isEnabled = false
            editTextReminder.isEnabled = false
        }
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
        if (!isEditPriceOnly) {
            serviceCallback.onServiceCreated(serviceDataModel = serviceDataModel, serviceGroupDataModel = serviceGroupDataModel, groupPosition = groupPosition, position = position)
        } else {
            prevServiceDataModel?.price = editTextPrice.text.toString().toLong()
            serviceGroupDataModel?.services?.set(position, prevServiceDataModel!!)
            serviceCallback.onEditService(prevServiceDataModel, serviceGroupDataModel, groupPosition, position)
        }
    }
}