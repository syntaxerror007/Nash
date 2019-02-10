package com.nash.android.service.form.service_group

import android.content.Context
import android.os.Bundle
import com.nash.android.R
import com.nash.android.core.dialog.CoreDialog
import com.nash.android.data.ServiceGroupDataModel
import com.nash.android.service.ServiceGroupCallback
import kotlinx.android.synthetic.main.service_group_form_dialog.*


class ServiceGroupFormDialog(context: Context, serviceGroupCallback: ServiceGroupCallback, serviceGroupDataModel: ServiceGroupDataModel?) : CoreDialog<ServiceGroupFormViewModel>(context) {
    private val serviceGroupCallback: ServiceGroupCallback = serviceGroupCallback
    private val serviceGroupDataModel = serviceGroupDataModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.service_group_form_dialog)
        setListener()
        initData()
    }

    private fun initData() {
        editTextLayoutServiceGroupName.setText(serviceGroupDataModel?.serviceGroupName)
    }

    private fun setListener() {
        buttonClose.setOnClickListener { dismiss() }
        btnCancel.setOnClickListener { dismiss() }
        btnRegister.setOnClickListener {
            if (!editTextLayoutServiceGroupName.text!!.isBlank())
                serviceGroupCallback.onCreateServiceGroup(serviceGroupDataModel, editTextLayoutServiceGroupName.text.toString())
            else
                editTextLayoutServiceGroupName.error = "Service Group Name is Required"
        }
    }
}