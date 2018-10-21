package com.android.nash.customer.customerservice.create

import android.app.Activity
import android.app.DatePickerDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.android.nash.R
import com.android.nash.core.activity.CoreActivity
import com.android.nash.data.*
import com.android.nash.util.DateUtil
import com.android.nash.util.convertToString
import com.android.nash.util.toNashDate
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener
import kotlinx.android.synthetic.main.customer_add_service_form_activity.*
import org.parceler.Parcels
import java.util.*

class CustomerAddServiceFormActivity : CoreActivity<CustomerAddServiceFormViewModel>() {
    private lateinit var mServiceGroupAdapter: SimpleArrayListAdapter<ServiceGroupDataModel>
    private lateinit var mServiceAdapter: SimpleArrayListAdapter<ServiceDataModel>
    private lateinit var mTherapistAdapter: SimpleArrayListAdapter<TherapistDataModel>
    private var serviceGroupSelected = false
    private var serviceSelected = false
    private var therapistSelected = false

    override fun onCreateViewModel(): CustomerAddServiceFormViewModel = ViewModelProviders.of(this).get(CustomerAddServiceFormViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_add_service_form_activity)

        getViewModel().setFormData(Parcels.unwrap(intent.extras?.getParcelable("customerFormData")))
        setOnClickListener()
        getViewModel().isLoading().observe(this, Observer {
            if (it != null && it) {
                showLoadingDialog()
            } else {
                hideLoadingDialog()
            }
        })

        getViewModel().getUserDataModel().observe(this, Observer {
            if (it != null) {
                getViewModel().initData()
            }
        })


        getViewModel().isAddServiceSuccess().observe(this, Observer {
            if (it != null && it) {
                val intent = Intent()
                intent.putExtra("isSuccess", true)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        })

        getViewModel().getServiceGroups().observe(this, Observer {
            initForm()
        })
        getViewModel().setLoading(true)
        editTextServiceDate.setOnClickListener { openCalendarDialog() }
    }

    private fun openCalendarDialog() {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, p1, p2, p3 ->
            editTextServiceDate.setText(NashDate(p3, p2 + 1, p1).convertToString())
        }, year, month, day)
        datePickerDialog.datePicker.maxDate = calendar.time.time
        datePickerDialog.show()
    }

    private fun initForm() {
        if (getViewModel().getServiceGroups().value != null) {
            mServiceGroupAdapter = SimpleArrayListAdapter(this, getViewModel().getServiceGroups().value!!)
            editTextServiceGroupName.setAdapter(mServiceGroupAdapter)
            editTextServiceGroupName.setOnItemSelectedListener(object : OnItemSelectedListener {
                override fun onNothingSelected() {
                    disableServiceName()
                    disableTherapistName()
                    disablePrice()
                    serviceGroupSelected = false
                }

                override fun onItemSelected(view: View?, position: Int, id: Long) {
                    if (getViewModel().getServiceGroups().value != null && position > 0 && position <= getViewModel().getServiceGroups().value!!.size) {
                        if (mServiceGroupAdapter.getItem(position) != null) {
                            mServiceAdapter = SimpleArrayListAdapter(this@CustomerAddServiceFormActivity, mServiceGroupAdapter.getItem(position)!!.services)
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
                    val therapists = getViewModel().getTherapistLiveData().value?.filter { it.uuid in selectedService.assignedTherapistSet }
                    serviceSelected = true
                    if (therapists != null) {
                        mTherapistAdapter = SimpleArrayListAdapter(this@CustomerAddServiceFormActivity, therapists)
                        editTextTherapist.setAdapter(mTherapistAdapter)
                    }
                    enableTherapistName()
                }

            })

            editTextTherapist.setOnItemSelectedListener(object : OnItemSelectedListener {
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
        btnRegister.setOnClickListener {
            if (isFormValid()) {
                val selectedServiceGroup = editTextServiceGroupName.selectedItem as ServiceGroupDataModel
                val selectedService = editTextServiceName.selectedItem as ServiceDataModel
                val selectedTherapist = editTextTherapist.selectedItem as TherapistDataModel
                val price = editTextPrice.text.toString().toLong()
                val treatmentDate = DateUtil.convertShownDateToNashDate(editTextServiceDate.text.toString())
                val toRemindCalendar = Calendar.getInstance()
                val lashType = editTextLashType.text.toString()
                toRemindCalendar.add(Calendar.DAY_OF_MONTH, selectedService.reminder)
                val toRemindDate = toRemindCalendar.toNashDate()

                getViewModel().registerServiceToCustomer(CustomerServiceDataModel(
                        customerUUID = if (getViewModel().customerUUID != null) getViewModel().customerUUID!! else "",
                        locationUUID = getViewModel().locationUUID!!,
                        serviceGroupUUID = selectedServiceGroup.uuid,
                        serviceUUID = selectedService.uuid,
                        therapistUUID = selectedTherapist.uuid,
                        treatmentDate = treatmentDate,
                        toRemindDate = toRemindDate,
                        lashType = lashType,
                        toRemindDateTimestamp = toRemindCalendar.time.time,
                        serviceGroup = selectedServiceGroup,
                        service = selectedService,
                        therapist = selectedTherapist,
                        price = price
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
        inputLayoutLashType.visibility = View.GONE
        editTextServiceDate.visibility = View.GONE
    }

    private fun enablePrice() {
        inputLayoutPrice.visibility = View.VISIBLE
        inputLayoutLashType.visibility = View.VISIBLE
        editTextServiceDate.visibility = View.VISIBLE
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
}