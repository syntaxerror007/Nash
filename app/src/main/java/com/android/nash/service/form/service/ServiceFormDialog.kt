package com.android.nash.service.form.service

import android.content.Context
import android.os.Bundle
import com.android.nash.R
import com.android.nash.core.dialog.CoreDialog
import com.android.nash.data.ServiceDataModel
import com.android.nash.data.ServiceGroupDataModel
import com.android.nash.service.form.ServiceCallback
import kotlinx.android.synthetic.main.service_form_dialog.*

class ServiceFormDialog(context: Context, serviceCallback: ServiceCallback, serviceGroupDataModel: ServiceGroupDataModel?, groupPosition: Int, position: Int, isEditPriceOnly: Boolean = false, serviceDataModel: ServiceDataModel? = null) : CoreDialog<ServiceFormViewModel>(context) {
    private var serviceCallback: ServiceCallback = serviceCallback
    private var serviceGroupDataModel: ServiceGroupDataModel? = serviceGroupDataModel
    private var groupPosition: Int = groupPosition
    private var position: Int = position
    private var isEditPriceOnly: Boolean = isEditPriceOnly
    private var prevServiceDataModel = serviceDataModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.service_form_dialog)
        if (prevServiceDataModel != null) {
            editTextServiceName.setText(prevServiceDataModel?.serviceName)
            editTextReminder.setText("${prevServiceDataModel?.reminder}")
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
        val reminder = editTextReminder.text.toString()
        if (prevServiceDataModel == null) {
            val serviceDataModel = ServiceDataModel(uuid = "", serviceName = serviceName, reminder = reminder.toInt(), shouldFreeText = false)
            serviceCallback.onServiceCreated(serviceDataModel = serviceDataModel, serviceGroupDataModel = serviceGroupDataModel, groupPosition = groupPosition, position = position)
        } else {
            prevServiceDataModel?.serviceName = serviceName
            prevServiceDataModel?.reminder = reminder.toInt()
            serviceGroupDataModel?.services?.set(position, prevServiceDataModel!!)
            serviceCallback.onEditService(prevServiceDataModel, serviceGroupDataModel, groupPosition, position)
        }
    }
}