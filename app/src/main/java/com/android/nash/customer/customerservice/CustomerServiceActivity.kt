package com.android.nash.customer.customerservice

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.android.nash.core.activity.CoreActivity
import com.android.nash.R
import com.android.nash.customer.customerservice.customerservicedialog.CustomerAddServiceFormDialog
import com.android.nash.customer.customerservice.customerservicedialog.CustomerAddServiceFormListener
import com.android.nash.data.CustomerServiceDataModel
import com.android.nash.util.convertToPrice
import kotlinx.android.synthetic.main.customer_service_activity.*
import kotlinx.android.synthetic.main.layout_service_customer_footer.*
import org.parceler.Parcels

class CustomerServiceActivity : CoreActivity<CustomerServiceViewModel>(), CustomerAddServiceFormListener {

    private lateinit var addServiceDialog: CustomerAddServiceFormDialog

    override fun onCreateViewModel(): CustomerServiceViewModel = ViewModelProviders.of(this).get(CustomerServiceViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_service_activity)

        observeViewModel()
        getViewModel().setLoading(true)
        getViewModel().setCustomerDataModel(Parcels.unwrap(intent.extras?.getParcelable("customerDataModel")))
        addServiceDialog = CustomerAddServiceFormDialog(this)
        buttonAddService.setOnClickListener {
            addServiceDialog = CustomerAddServiceFormDialog(this)
            addServiceDialog.setFormData(getViewModel().getFormData())
            addServiceDialog.setOnSubmitListener(this)
            addServiceDialog.show()
        }
    }

    private fun observeViewModel() {
        getViewModel().getCustomerLiveData().observe(this, Observer {
            if (it != null) {
                getViewModel().loadServiceFromCustomer()
                setCustomerDataToUI()
            }
        })

        getViewModel().getCustomerServiceLiveData().observe(this, Observer {
            if (it != null)
                observeCustomerService(it)
        })

        getViewModel().isLoading().observe(this, Observer {
            if (it != null && it) {
                showLoadingDialog()
            } else {
                hideLoadingDialog()
            }
        })

        getViewModel().isAddServiceSuccess().observe(this, Observer {
            if (addServiceDialog.isShowing)
                addServiceDialog.dismiss()
        })

        getViewModel().getServiceGroups().observe(this, Observer {
            if (addServiceDialog.isShowing) {
                addServiceDialog.setFormData(getViewModel().getFormData())
            }
            getViewModel().setLoading(false)
        })

        getViewModel().getTherapistLiveData().observe(this, Observer {
            if (addServiceDialog.isShowing) {
                addServiceDialog.setFormData(getViewModel().getFormData())
            }
            getViewModel().setLoading(false)
        })

        getViewModel().getUserDataModel().observe(this, Observer {
            if (it != null) {
                getViewModel().initData()
                getViewModel().getCustomerServices()
            }
        })

        getViewModel().getTotalServicePriceLiveData().observe(this, Observer {
            if (it != null) {
                textViewTotalValue.text = it.convertToPrice()
            }
        })
    }


    private fun observeCustomerService(it: List<CustomerServiceDataModel>) {
        recyclerViewService.adapter = CustomerServiceAdapter(it)
    }

    private fun setCustomerDataToUI() {

    }


    override fun onSubmit(customerServiceDataModel: CustomerServiceDataModel) {
        getViewModel().addNewService(customerServiceDataModel)
    }

    override fun onCancel() {
        if (addServiceDialog.isShowing)
            addServiceDialog.dismiss()
    }
}