package com.android.nash.customer.newcustomer

import android.app.DatePickerDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.widget.Toast
import com.android.nash.R
import com.android.nash.core.activity.CoreActivity
import com.android.nash.core.yesnocheckbox.MultiCheckCheckGroupData
import com.android.nash.data.CustomerDataModel
import com.android.nash.data.NashDate
import com.android.nash.util.DateUtil
import com.android.nash.util.convertToString
import kotlinx.android.synthetic.main.customer_new_form_activity.*
import java.util.*

class CustomerNewFormActivity : CoreActivity<CustomerNewFormViewModel>() {
    override fun onCreateViewModel(): CustomerNewFormViewModel = ViewModelProviders.of(this).get(CustomerNewFormViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_new_form_activity)
        setTitle(R.string.text_new_customer_form_title)
        observeViewModel()
        setupMultiCheckGroups()
        setupCalendarDialog()
        buttonSave.setOnClickListener { saveCustomer() }
    }

    private fun observeViewModel() {
        getViewModel().isLoading().observe(this, Observer { observeLoading(it!!) })
        getViewModel().isSuccess().observe(this, Observer { finish() })
        getViewModel().getErrorMessage().observe(this, Observer { Toast.makeText(this, it!!, Toast.LENGTH_LONG).show() })
    }

    private fun observeLoading(it: Boolean) {
        if (it) {
            showLoadingDialog()
        } else {
            hideLoadingDialog()
        }
    }

    private fun setupCalendarDialog() {
        editTextBirthDate.setOnClickListener { openCalendarDialog() }
    }

    private fun openCalendarDialog() {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, p1, p2, p3 ->
            editTextBirthDate.setText(NashDate(p3, p2+1, p1).convertToString())
        }, year, month, day)
        datePickerDialog.datePicker.maxDate = calendar.time.time
        datePickerDialog.show()
    }

    private fun saveCustomer() {
        val customerDataModel = CustomerDataModel()
        if (!isValid(editTextCustomerName.text.toString())) {
            editTextCustomerName.error = "Please put customer name"
            return
        }
        if (!isValid(editTextBirthDate.text.toString())) {
            editTextBirthDate.error = "Please put birth date"
            return
        }
        if (!isValid(editTextAddress.text.toString())) {
            editTextAddress.error = "Please put address"
            return
        }
        if (!isValid(editTextEmail.text.toString())) {
            editTextEmail.error = "Please put email"
            return
        }
        if (!isValid(editTextPhoneNumber.text.toString())) {
            editTextPhoneNumber.error = "Please put email"
            return
        }

        if (!checkBoxAgreeTnC.isChecked) {
            return
        }

        customerDataModel.customerName = editTextCustomerName.text.toString()
        customerDataModel.customerLowerCase = editTextCustomerName.text.toString().toLowerCase()
        customerDataModel.customerDateOfBirth = DateUtil.convertShownDateToNashDate(editTextBirthDate.text.toString())
        customerDataModel.customerAddress = editTextAddress.text.toString()
        customerDataModel.customerEmail = editTextEmail.text.toString()
        customerDataModel.customerPhone = editTextPhoneNumber.text.toString()
        val hasEyelashResult = hasEyelashBeforeMCG.getMultiCheckData()
        val haveAllergyResult = haveAllergyMCG.getMultiCheckData()
        val wearContactLensResult = wearContactLensMCG.getMultiCheckData()
        val hadEyeSurgeryResult = hadEyeSurgeryMCG.getMultiCheckData()
        val knowNashResult = knowNashMCG.getMultiCheckData()

        customerDataModel.hasExtensions = !(hasEyelashResult.selectedItems.size == 0 || hasEyelashResult.selectedItems[0].equals("NO", true))
        customerDataModel.hasExtensionsInfo = hasEyelashResult.additionalData

        customerDataModel.hasAllergy = !(haveAllergyResult.selectedItems.size == 0 || haveAllergyResult.selectedItems[0].equals("NO", true))
        customerDataModel.hasAllergyInfo = haveAllergyResult.additionalData

        customerDataModel.wearContactLens = !(wearContactLensResult.selectedItems.size == 0 || hasEyelashResult.selectedItems[0].equals("NO", true))
        customerDataModel.wearContactLensInfo = wearContactLensResult.additionalData

        customerDataModel.hadSurgery = !(hadEyeSurgeryResult.selectedItems.size == 0 || hasEyelashResult.selectedItems[0].equals("NO", true))
        customerDataModel.hadSurgeryInfo = hadEyeSurgeryResult.additionalData


        customerDataModel.knowNashFrom = knowNashResult.selectedItems[0]
        customerDataModel.knowNashFromInfo = knowNashResult.additionalData


        getViewModel().insertCustomer(customerDataModel)
    }

    private fun isValid(input: String): Boolean {
        return !input.isBlank()
    }

    private fun setupMultiCheckGroups() {
        val hasEyelashBeforeMCGData = MultiCheckCheckGroupData("hasEyelash", getString(R.string.text_has_eyelash_before_title))
        hasEyelashBeforeMCG.setMultiCheckData(hasEyelashBeforeMCGData)
        val haveAllergyMCGData = MultiCheckCheckGroupData("haveAllergy", getString(R.string.text_have_allergy))
        haveAllergyMCG.setMultiCheckData(haveAllergyMCGData)
        val wearContactLensMCGData = MultiCheckCheckGroupData("wearContactLens", getString(R.string.text_wear_contact_lens))
        wearContactLensMCG.setMultiCheckData(wearContactLensMCGData)
        val hadEyeSurgeryMCGData = MultiCheckCheckGroupData("hadEyeSurgery", getString(R.string.text_had_surgery))
        hadEyeSurgeryMCG.setMultiCheckData(hadEyeSurgeryMCGData)

        val knowNashMCGData = MultiCheckCheckGroupData("hasEyelash", getString(R.string.text_know_nash), options = resources.getStringArray(R.array.list_know_nash_options).toList(), enableMultiCheck = true)
        knowNashMCG.setMultiCheckData(knowNashMCGData)
    }
}