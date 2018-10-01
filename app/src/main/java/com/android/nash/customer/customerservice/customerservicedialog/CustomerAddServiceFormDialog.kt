package com.android.nash.customer.customerservice.customerservicedialog

import android.content.Context
import android.os.Bundle
import android.view.View
import com.android.nash.core.CoreViewModel
import com.android.nash.core.dialog.CoreDialog
import com.android.nash.R
import com.android.nash.data.ServiceDataModel
import com.android.nash.data.ServiceGroupDataModel
import com.android.nash.data.TherapistDataModel
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener
import kotlinx.android.synthetic.main.customer_add_service_form_dialog.*

class CustomerAddServiceFormDialog(context: Context) : CoreDialog<CustomerAddServiceFormViewModel>(context) {
    private lateinit var mListener: CustomerAddServiceFormListener
    private lateinit var mServiceGroupAdapter: SimpleArrayListAdapter<ServiceGroupDataModel>
    private lateinit var mServiceAdapter: SimpleArrayListAdapter<ServiceDataModel>
    private lateinit var mTherapistAdapter: SimpleArrayListAdapter<TherapistDataModel>

    init {
        setViewModel(CustomerAddServiceFormViewModel())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_add_service_form_dialog)
        if (getViewModel().serviceGroupDataModel != null) {
            mServiceGroupAdapter = SimpleArrayListAdapter(context, getViewModel().serviceGroupDataModel!!)
            editTextServiceGroupName.setAdapter(mServiceGroupAdapter)
            editTextServiceGroupName.setOnItemSelectedListener(object : OnItemSelectedListener {
                override fun onNothingSelected() {
                    disableServiceName()
                }

                override fun onItemSelected(view: View?, position: Int, id: Long) {
                    if (getViewModel().serviceGroupDataModel != null && position > 0 && position < getViewModel().serviceGroupDataModel!!.size) {
                        enableServiceName()
                        mServiceAdapter = SimpleArrayListAdapter(context, getViewModel().serviceGroupDataModel!![position].services)

                        editTextServiceName.setAdapter(mServiceAdapter)
                    }
                }

            })
            editTextServiceName.setOnItemSelectedListener(object : OnItemSelectedListener {
                override fun onNothingSelected() {
                    disableTherapistName()
                }

                override fun onItemSelected(view: View?, position: Int, id: Long) {
                    val services = getViewModel().serviceGroupDataModel!![editTextServiceName.selectedPosition].services
                    if (getViewModel().serviceGroupDataModel != null && position < services.size && position > 0) {
                        enableTherapistName()
//                        getViewModel().getTherapistFromService(services[position].uuid)
                    }
                }

            })
        }
    }

    private fun enableTherapistName() {
        editTextTherapist.isClickable = true
        editTextTherapist.isEnabled = true
    }

    private fun disableTherapistName() {
        editTextTherapist.isClickable = false
        editTextTherapist.isEnabled = false
    }

    private fun enableServiceName() {
        editTextServiceName.isClickable = true
        editTextServiceName.isEnabled = true

    }

    private fun disableServiceName() {
        editTextServiceName.isClickable = false
        editTextServiceName.isEnabled = false
    }

    fun setOnSubmitListener(mListener: CustomerAddServiceFormListener) {
        this.mListener = mListener
    }

    fun setFormData(formData: CustomerServiceDialogFormData) {
        getViewModel().setFormData(formData)
    }

    fun setTherapistData(therapist: List<TherapistDataModel>?) {

    }
}