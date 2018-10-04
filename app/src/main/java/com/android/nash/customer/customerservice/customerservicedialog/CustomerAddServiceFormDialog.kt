package com.android.nash.customer.customerservice.customerservicedialog

import android.content.Context
import android.os.Bundle
import android.view.View
import com.android.nash.core.dialog.CoreDialog
import com.android.nash.R
import com.android.nash.data.CustomerServiceDataModel
import com.android.nash.data.ServiceDataModel
import com.android.nash.data.ServiceGroupDataModel
import com.android.nash.data.TherapistDataModel
import com.android.nash.util.toNashDate
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener
import kotlinx.android.synthetic.main.customer_add_service_form_dialog.*
import java.util.*

class CustomerAddServiceFormDialog(context: Context) : CoreDialog<CustomerAddServiceFormViewModel>(context) {
    private lateinit var mListener: CustomerAddServiceFormListener
    private lateinit var mServiceGroupAdapter: SimpleArrayListAdapter<ServiceGroupDataModel>
    private lateinit var mServiceAdapter: SimpleArrayListAdapter<ServiceDataModel>
    private lateinit var mTherapistAdapter: SimpleArrayListAdapter<TherapistDataModel>
    private var serviceGroupSelected = false
    private var serviceSelected = false
    private var therapistSelected = false

    init {
        setViewModel(CustomerAddServiceFormViewModel())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_add_service_form_dialog)
        initDialogSize()
        setOnClickListener()
        if (getViewModel().serviceGroupDataModel != null) {
            mServiceGroupAdapter = SimpleArrayListAdapter(context, getViewModel().serviceGroupDataModel!!)
            editTextServiceGroupName.setAdapter(mServiceGroupAdapter)
            editTextServiceGroupName.setOnItemSelectedListener(object : OnItemSelectedListener {
                override fun onNothingSelected() {
                    disableServiceName()
                    disableTherapistName()
                    disablePrice()
                    serviceGroupSelected = false
                }

                override fun onItemSelected(view: View?, position: Int, id: Long) {
                    if (getViewModel().serviceGroupDataModel != null && position > 0 && position <= getViewModel().serviceGroupDataModel!!.size) {
                        if (mServiceGroupAdapter.getItem(position) != null) {
                            mServiceAdapter = SimpleArrayListAdapter(context, mServiceGroupAdapter.getItem(position)!!.services)
                            editTextServiceName.setAdapter(mServiceAdapter)
                            enableServiceName()
                            serviceGroupSelected = true
                        }
                    } else {
                        serviceGroupSelected = false
                        disableServiceName()
                    }
                }

            })
            editTextServiceName.setOnItemSelectedListener(object : OnItemSelectedListener {
                override fun onNothingSelected() {
                    disableTherapistName()
                    disablePrice()
                    serviceSelected = false
                }

                override fun onItemSelected(view: View?, position: Int, id: Long) {
                    val services = (editTextServiceGroupName.selectedItem as ServiceGroupDataModel).services
                    val selectedService = editTextServiceName.selectedItem as ServiceDataModel
                    val therapists = getViewModel().therapistDataModel?.filter { it.uuid in selectedService.assignedTherapistSet }
                    serviceSelected = true
                    if (therapists != null) {
                        mTherapistAdapter = SimpleArrayListAdapter(context, therapists)
                        editTextTherapist.setAdapter(mTherapistAdapter)
                    }
                    enableTherapistName()
                }

            })

            editTextTherapist.setOnItemSelectedListener(object: OnItemSelectedListener {
                override fun onNothingSelected() {
                    disablePrice()
                    therapistSelected = false
                }

                override fun onItemSelected(view: View?, position: Int, id: Long) {
                    enablePrice()
                    therapistSelected = true
                }

            })
        }
    }

    private fun setOnClickListener() {
        buttonClose.setOnClickListener { cancel() }
        btnCancel.setOnClickListener { cancel() }
        btnRegister.setOnClickListener {
            if (isFormValid()) {
                val selectedServiceGroup = editTextServiceGroupName.selectedItem as ServiceGroupDataModel
                val selectedService = editTextServiceName.selectedItem as ServiceDataModel
                val selectedTherapist = editTextTherapist.selectedItem as TherapistDataModel
                val price = editTextPrice.text.toString().toLong()
                val treatmentDate = Calendar.getInstance().toNashDate()
                val toRemindCalendar = Calendar.getInstance()
                toRemindCalendar.add(Calendar.DAY_OF_MONTH, selectedService.reminder)
                val toRemindDate = toRemindCalendar.toNashDate()

                mListener.onSubmit(CustomerServiceDataModel(
                        customerUUID = if (getViewModel().customerUUID != null) getViewModel().customerUUID!! else "",
                        locationUUID = getViewModel().locationUUID!!,
                        therapist = selectedTherapist,
                        service = selectedService,
                        serviceUUID = selectedService.uuid,
                        serviceGroup = selectedServiceGroup,
                        serviceGroupUUID = selectedServiceGroup.uuid,
                        therapistUUID = selectedTherapist.uuid,
                        price = price,
                        treatmentDate = treatmentDate,
                        toRemindDate = toRemindDate
                        ))
            }
        }
    }

    private fun isFormValid(): Boolean {
        if (!serviceGroupSelected) {
    //                editTextServiceGroupNameError.visibility = View.VISIBLE
            return false
        } else {
    //                editTextServiceGroupNameError.visibility = View.GONE
        }
        if (!serviceSelected) {
    //                editTextServiceGroupNameError.visibility = View.VISIBLE
            return false
        } else {
    //                editTextServiceGroupNameError.visibility = View.GONE
        }
        if (!therapistSelected) {
    //                editTextServiceGroupNameError.visibility = View.VISIBLE
            return false
        } else {
    //                editTextServiceGroupNameError.visibility = View.GONE
        }
        if (editTextPrice.text.toString().isEmpty()) {
            editTextPrice.error = "Please input price"
            return false
        }
        return true
    }

    private fun disablePrice() {
        inputLayoutPrice.visibility = View.GONE
    }

    private fun enablePrice() {
        inputLayoutPrice.visibility = View.VISIBLE
    }

    private fun enableTherapistName() {
        editTextTherapist.visibility = View.VISIBLE
    }

    private fun disableTherapistName() {
        editTextTherapist.visibility = View.GONE
    }

    private fun enableServiceName() {
        editTextServiceName.visibility = View.VISIBLE

    }

    private fun disableServiceName() {
        editTextServiceName.visibility = View.GONE
    }

    fun setOnSubmitListener(mListener: CustomerAddServiceFormListener) {
        this.mListener = mListener
    }

    fun setFormData(formData: CustomerServiceDialogFormData) {
        getViewModel().setFormData(formData)
    }
}