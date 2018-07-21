package com.android.nash.service.form

import android.content.Context
import android.os.Bundle
import com.android.nash.R
import com.android.nash.core.dialog.CoreDialog
import com.android.nash.service.ServiceGroupCallback
import kotlinx.android.synthetic.main.service_form_dialog.*



class ServiceFormDialog(context: Context) : CoreDialog<ServiceFormViewModel>(context) {
    private lateinit var serviceGroupCallback: ServiceGroupCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.service_form_dialog)
        val metrics = context.resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        this.window.setLayout(6 * width / 7, 4 * height / 5)

        buttonClose.setOnClickListener { dismiss() }
        btnCancel.setOnClickListener { dismiss() }
        btnRegister.setOnClickListener { serviceGroupCallback.onCreateService(editTextLayoutServiceGroupName.text.toString()) }
    }

    fun setServiceGroupCallback(serviceGroupCallback: ServiceGroupCallback) {
        this.serviceGroupCallback = serviceGroupCallback
    }
}