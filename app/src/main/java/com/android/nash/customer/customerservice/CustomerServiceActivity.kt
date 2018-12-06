package com.android.nash.customer.customerservice

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import com.android.nash.R
import com.android.nash.core.activity.CoreActivity
import com.android.nash.customer.customerdetail.CustomerDetailActivity
import com.android.nash.customer.customerservice.create.CustomerAddServiceFormActivity
import com.android.nash.data.CustomerServiceDataModel
import com.android.nash.data.UserDataModel
import com.android.nash.util.convertToPrice
import com.android.nash.util.setVisible
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
            initToolbarButton()
            buttonAddService.setVisible(false)
        }
    }

    private fun initToolbarButton() {
        setPrimaryButtonImage(R.drawable.ic_menu)
        setSecondaryButtonImage(R.drawable.ic_launcher_background)
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
        startActivity(Intent(this, CustomerDetailActivity::class.java).putExtras(bundle))
        finish()
    }

    private fun observeCustomerService(it: List<CustomerServiceDataModel>) {
        if (it.isEmpty()) {
            recyclerViewHeader.setVisible(false)
            recyclerViewFooter.setVisible(false)
        } else {
            recyclerViewHeader.setVisible(true)
            recyclerViewFooter.setVisible(true)
        }
        recyclerViewService.adapter = CustomerServiceAdapter(it) {
            it.customerDataModel = getViewModel().getCustomerLiveData().value!!
            val dialog = CustomerServiceDetailDialog(this, it)
            dialog.show()
        }
    }
}