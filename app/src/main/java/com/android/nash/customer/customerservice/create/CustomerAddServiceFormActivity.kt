package com.android.nash.customer.customerservice.create

import android.app.Activity
import android.app.DatePickerDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.ajithvgiri.searchdialog.SearchListItem
import com.ajithvgiri.searchdialog.SearchableDialog
import com.android.nash.R
import com.android.nash.core.activity.CoreActivity
import com.android.nash.data.*
import com.android.nash.util.DateUtil
import com.android.nash.util.convertToCalendar
import com.android.nash.util.convertToString
import com.android.nash.util.toNashDate
import kotlinx.android.synthetic.main.customer_add_service_form_activity.*
import org.parceler.Parcels
import java.util.*


class CustomerAddServiceFormActivity : CoreActivity<CustomerAddServiceFormViewModel>() {
    //    private lateinit var mServiceGroupAdapter: SimpleArrayListAdapter<ServiceGroupDataModel>
//    private lateinit var mServiceAdapter: SimpleArrayListAdapter<ServiceDataModel>
//    private lateinit var mTherapistAdapter: SimpleArrayListAdapter<TherapistDataModel>
    private var serviceGroupSelected = false
    private var serviceSelected = false
    private var therapistSelected = false
    private var selectedServiceGroup: ServiceGroupDataModel? = null
    private var selectedService: ServiceDataModel? = null
    private var selectedTherapist: TherapistDataModel? = null

    override fun onCreateViewModel(): CustomerAddServiceFormViewModel = ViewModelProviders.of(this).get(CustomerAddServiceFormViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_add_service_form_activity)

        getViewModel().setFormData(Parcels.unwrap(intent.extras?.getParcelable("customerFormData")))

        setTitle("Add New Treatment - ${getViewModel().customerName}")
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
            initSearchServiceGroupAutoComplete()
            btnRegister.visibility = View.GONE
        }
    }

    private fun initSearchServiceGroupAutoComplete() {
        val serviceGroupSearchListItem = getViewModel().getServiceGroups().value!!.mapIndexed { index, serviceGroupDataModel ->
            SearchListItem(index, serviceGroupDataModel.serviceGroupName)
        }
        editTextServiceGroupName.isFocusable = false
        editTextServiceGroupName.isClickable = true
        editTextServiceGroupName.setTextColor(ContextCompat.getColor(this, R.color.black))
        val serviceGroupSearchableDialog = SearchableDialog(this, serviceGroupSearchListItem, "Service Group")
        serviceGroupSearchableDialog.setOnItemSelected { position, _ ->
            if (getViewModel().getServiceGroups().value != null && position < getViewModel().getServiceGroups().value!!.size) {
                val serviceGroups = getViewModel().getServiceGroups().value!!
                selectedServiceGroup = serviceGroups[position]
                val serviceSearchListItem = selectedServiceGroup?.services?.mapIndexed { index, service ->
                    SearchListItem(index, service.serviceName)
                }
                editTextServiceGroupName.setText(selectedServiceGroup?.serviceGroupName)
                enableServiceName(serviceSearchListItem)
                disableTherapistName()
                disablePrice()

                resetTherapist()
                resetService()
                resetPrice()
                serviceGroupSelected = true
            } else {
                disableServiceName()
                disableTherapistName()
                disablePrice()

                resetTherapist()
                resetService()
                resetPrice()
            }
        }
        editTextServiceGroupName.setOnClickListener {
            serviceGroupSearchableDialog.show()
        }
    }

    private fun initSearchServiceAutoComplete(serviceSearchListItem: List<SearchListItem>?) {
        val serviceSearchableDialog = SearchableDialog(this, serviceSearchListItem, "Select Treatment")
        editTextServiceName.isFocusable = false
        editTextServiceName.isClickable = true
        serviceSearchableDialog.setOnItemSelected { position, _ ->
            val services = selectedServiceGroup?.services
            selectedService = services?.elementAt(position)
            val therapists = getViewModel().getTherapistLiveData().value?.filter {
                selectedService?.assignedTherapistSet?.contains(it.uuid) ?: false
            }
            editTextServiceName.setText(selectedService?.serviceName)
            serviceSelected = true
            if (therapists != null) {
                enableTherapistName(therapists.mapIndexed { index, therapist ->
                    SearchListItem(index, therapist.therapistName)
                })
                resetTherapist()
                resetPrice()
                disablePrice()
            }
        }

        editTextServiceName.setOnClickListener {
            serviceSearchableDialog.show()
        }
    }


    private fun initSearchTherapistAutoComplete(therapists: List<SearchListItem>) {
        val therapistSearchableDialog = SearchableDialog(this, therapists, "Select Therapist")
        editTextTherapist.isFocusable = false
        editTextTherapist.isClickable = true
        therapistSearchableDialog.setOnItemSelected { _, therapistName ->
            selectedTherapist = getViewModel().getTherapistLiveData().value?.find {
                it.therapistName == therapistName.title
            }
            editTextTherapist.setText(selectedTherapist?.therapistName)
            therapistSelected = true
            resetPrice()
            enablePrice()
        }
        editTextTherapist.setOnClickListener {
            therapistSearchableDialog.show()
        }
    }

    private fun setOnClickListener() {
        btnRegister.setOnClickListener {
            if (isFormValid()) {
                val selectedServiceGroup = selectedServiceGroup
                val selectedService = selectedService
                val selectedTherapist = selectedTherapist
                val price = editTextPrice.text.toString().toLong()
                val treatmentDate = DateUtil.convertShownDateToNashDate(editTextServiceDate.text.toString())
                val treatmentDateCalendar = treatmentDate.convertToCalendar()
                val toRemindCalendar = treatmentDateCalendar.clone() as Calendar
                val lashType = editTextLashType.text.toString()
                val toRemindDate = toRemindCalendar.toNashDate()

                getViewModel().registerServiceToCustomer(CustomerServiceDataModel(
                        customerUUID = if (getViewModel().customerUUID != null) getViewModel().customerUUID!! else "",
                        locationUUID = getViewModel().locationUUID!!,
                        serviceGroupUUID = selectedServiceGroup!!.uuid,
                        serviceUUID = selectedService!!.uuid,
                        therapistUUID = selectedTherapist!!.uuid,
                        treatmentDate = treatmentDate,
                        treatmentDateTimestamp = treatmentDateCalendar.timeInMillis,
                        toRemindDate = toRemindDate,
                        lashType = lashType,
                        toRemindDateTimestamp = toRemindCalendar.timeInMillis,
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
        btnRegister.visibility = View.GONE
    }

    private fun enablePrice() {
        inputLayoutPrice.visibility = View.VISIBLE
        inputLayoutLashType.visibility = View.VISIBLE
        editTextServiceDate.visibility = View.VISIBLE
        btnRegister.visibility = View.VISIBLE
    }

    private fun enableTherapistName(therapists: List<SearchListItem>) {
        initSearchTherapistAutoComplete(therapists)
        editTextTherapist.visibility = View.VISIBLE
    }


    private fun disableTherapistName() {
        editTextTherapist.visibility = View.GONE
    }

    private fun enableServiceName(serviceSearchListItem: List<SearchListItem>?) {
        initSearchServiceAutoComplete(serviceSearchListItem)
        editTextServiceName.visibility = View.VISIBLE

    }

    private fun disableServiceName() {
        editTextServiceName.visibility = View.GONE
    }

    private fun resetTherapist() {
        editTextTherapist.text = null
        selectedTherapist = null
        therapistSelected = false
    }

    private fun resetService() {
        editTextServiceName.text = null
        selectedService = null
        serviceSelected = false
    }

    private fun resetPrice() {
        editTextPrice.text = null
    }
}
