package com.nash.android.customer.customerservice

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import com.nash.android.R
import com.nash.android.core.activity.CoreActivity
import com.nash.android.customer.customerservice.create.CustomerAddServiceFormActivity
import com.nash.android.customer.newcustomer.CustomerNewFormActivity
import com.nash.android.data.CustomerServiceDataModel
import com.nash.android.data.UserDataModel
import com.nash.android.util.convertToPrice
import com.nash.android.util.setVisible
import kotlinx.android.synthetic.main.customer_service_activity.*
import kotlinx.android.synthetic.main.layout_service_customer_footer.*
import org.parceler.Parcels

class CustomerServiceActivity : CoreActivity<CustomerServiceViewModel>() {

    override fun onCreateViewModel(): CustomerServiceViewModel = ViewModelProviders.of(this).get(CustomerServiceViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_service_activity)

        observeViewModel()
        getViewModel().setCustomerDataModel(Parcels.unwrap(intent.extras?.getParcelable("customerDataModel")))
        buttonAddService.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("customerFormData", Parcels.wrap(getViewModel().getFormData()))
            startActivityForResult(Intent(this@CustomerServiceActivity, CustomerAddServiceFormActivity::class.java).putExtras(bundle), 1)
        }
        initToolbarButton()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                getViewModel().getCustomerServices()
            }
        }
    }

    private fun observeViewModel() {
        getViewModel().getCustomerLiveData().observe(this, Observer {
            if (it != null) {
                setTitle("${it.customerName} - Services")
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

        getViewModel().getUserDataModel().observe(this, Observer {
            if (it != null) {
                getViewModel().initData()
                getViewModel().getCustomerServices()
                initView(it)
            }
        })

        getViewModel().getTotalServicePriceLiveData().observe(this, Observer {
            if (it != null) {
                textViewTotalValue.text = it.convertToPrice()
            }
        })
    }

    private fun initView(it: UserDataModel) {
        if (it.userType.equals("ADMIN", true)) {
            buttonAddService.setVisible(false)
        }
    }

    private fun initToolbarButton() {
        setPrimaryButtonImage(R.drawable.ic_customer_gray)
        setSecondaryButtonImage(R.drawable.ic_service_white)
        showPrimaryButton()
        showSecondaryButton()
        setPrimaryButtonClick {
            navigateToCustomerDetail()
        }

        setSecondaryButtonClick {
        }
    }

    private fun navigateToCustomerDetail() {
        val bundle = Bundle()
        bundle.putParcelable("customerDataModel", Parcels.wrap(getViewModel().getCustomerData()))
        startActivity(Intent(this, CustomerNewFormActivity::class.java).putExtras(bundle))
        finish()
    }

    private fun observeCustomerService(it: List<CustomerServiceDataModel>) {
        if (it.isEmpty()) {
            recyclerViewHeader.setVisible(false)
        } else {
            recyclerViewHeader.setVisible(true)
        }
        recyclerViewService.adapter = CustomerServiceAdapter(it) {
            it.customerDataModel = getViewModel().getCustomerLiveData().value!!
            val dialog = CustomerServiceDetailDialog(this, it)
            dialog.show()
        }
    }
}