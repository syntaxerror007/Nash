package com.android.nash.service.form.service_group

import android.content.Context
import android.os.Bundle
import com.android.nash.R
import com.android.nash.core.dialog.CoreDialog
import com.android.nash.service.ServiceGroupCallback
import kotlinx.android.synthetic.main.service_group_form_dialog.*


class ServiceGroupFormDialog(context: Context, serviceGroupCallback: ServiceGroupCallback) : CoreDialog<ServiceGroupFormViewModel>(context) {
    private val serviceGroupCallback: ServiceGroupCallback = serviceGroupCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.service_group_form_dialog)
        initDialogSize()
        setListener()
    }

    private fun setListener() {
        buttonClose.setOnClickListener { dismiss() }
        btnCancel.setOnClickListener { dismiss() }
        btnRegister.setOnClickListener {
            if (!editTextLayoutServiceGroupName.text.isBlank())
                serviceGroupCallback.onCreateServiceGroup(editTextLayoutServiceGroupName.text.toString())
            else
                editTextLayoutServiceGroupName.error = "Service Group Name is Required"
        }
    }
}