package com.android.nash.customer.customerservice.customerservicedialog

import android.content.Context
import android.os.Bundle
import com.android.nash.core.CoreViewModel
import com.android.nash.core.dialog.CoreDialog
import com.android.nash.R

class CustomerAddServiceFormDialog(context: Context) : CoreDialog<CoreViewModel>(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_add_service_form_dialog)
    }
}