package com.android.nash.customer.customerdetail

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import com.android.nash.R
import com.android.nash.core.activity.CoreActivity
import com.android.nash.customer.customerservice.CustomerServiceActivity
import com.android.nash.data.CustomerDataModel
import com.android.nash.util.convertToString
import com.android.nash.util.setVisible
import kotlinx.android.synthetic.main.customer_detail_activity.*
import org.parceler.Parcels

class CustomerDetailActivity : CoreActivity<CustomerDetailViewModel>() {
    override fun onCreateViewModel(): CustomerDetailViewModel =
            ViewModelProviders.of(this).get(CustomerDetailViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_detail_activity)
        val customerServiceDataModel = Parcels.unwrap(intent.extras?.getParcelable("customerDataModel")) as CustomerDataModel
        setData(customerDataModel = customerServiceDataModel)
        initToolbarButton()
    }

    private fun initToolbarButton() {
        setPrimaryButtonImage(R.drawable.ic_menu)
        setSecondaryButtonImage(R.drawable.ic_launcher_background)
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