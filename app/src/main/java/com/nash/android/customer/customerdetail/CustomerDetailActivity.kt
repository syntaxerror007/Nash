package com.nash.android.customer.customerdetail

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import com.nash.android.R
import com.nash.android.core.activity.CoreActivity
import com.nash.android.customer.customerservice.CustomerServiceActivity
import com.nash.android.data.CustomerDataModel
import com.nash.android.util.convertToString
import com.nash.android.util.setVisible
import kotlinx.android.synthetic.main.customer_detail_activity.*
import org.parceler.Parcels

class CustomerDetailActivity : CoreActivity<CustomerDetailViewModel>() {
    override fun onCreateViewModel(): CustomerDetailViewModel =
            ViewModelProviders.of(this).get(CustomerDetailViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_detail_activity)
        setTitle("Customer Detail")
        val customerServiceDataModel = Parcels.unwrap(intent.extras?.getParcelable("customerDataModel")) as CustomerDataModel
        setData(customerDataModel = customerServiceDataModel)
        initToolbarButton()
    }

    private fun initToolbarButton() {
        setPrimaryButtonImage(R.drawable.ic_customer_white)
        setSecondaryButtonImage(R.drawable.ic_service_gray)
        showPrimaryButton()
        showSecondaryButton()
        setPrimaryButtonClick {

        }

        setSecondaryButtonClick {
            navigateToCustomerServiceList()
        }
    }

    private fun navigateToCustomerServiceList() {
        val bundle = Bundle()
        bundle.putParcelable("customerDataModel", Parcels.wrap(getViewModel().getCustomerData()))
        startActivity(Intent(this, CustomerServiceActivity::class.java).putExtras(bundle))
        finish()
    }

    private fun setData(customerDataModel: CustomerDataModel) {
        customerName.text = customerDataModel.customerName
        customerPhone.text = customerDataModel.customerPhone
        customerAddress.text = customerDataModel.customerAddress
        customerEmail.text = customerDataModel.customerEmail
        customerDateOfBirth.text = customerDataModel.customerDateOfBirth.convertToString()
        hasEyelashBefore.text = if (customerDataModel.hasExtensions) "YES" else "NO"
        hadEyeSurgery.text = if (customerDataModel.hadSurgery) "YES" else "NO"
        wearContactLens.text = if (customerDataModel.wearContactLens) "YES" else "NO"
        haveAllergy.text = if (customerDataModel.hasAllergy) "YES" else "NO"
        haveAllergyAdditional.setVisible(customerDataModel.hasAllergyInfo.isNotEmpty())
        hadEyeSurgeryAdditional.setVisible(customerDataModel.hadSurgeryInfo.isNotEmpty())
        hasEyelashBeforeAdditional.setVisible(customerDataModel.hasExtensionsInfo.isNotEmpty())
        wearContactLensAdditional.setVisible(customerDataModel.wearContactLensInfo.isNotEmpty())
        haveAllergyAdditional.text = customerDataModel.hasAllergyInfo
        hadEyeSurgeryAdditional.text = customerDataModel.hadSurgeryInfo
        hasEyelashBeforeAdditional.text = customerDataModel.hasExtensionsInfo
        wearContactLensAdditional.text = customerDataModel.wearContactLensInfo
        knowNash.text = customerDataModel.knowNashFrom.joinToString(separator = ", ")
        knowNash.setVisible(customerDataModel.knowNashFrom.isNotEmpty())
        knowNashAdditional.text = customerDataModel.knowNashFromInfo
        knowNashAdditional.setVisible(customerDataModel.knowNashFromInfo.isNotEmpty())
        getViewModel().setCustomerDataModel(customerDataModel)
    }
}