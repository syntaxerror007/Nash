package com.nash.android.customer.newcustomer

import android.app.DatePickerDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.nash.android.R
import com.nash.android.core.activity.CoreActivity
import com.nash.android.core.yesnocheckbox.MultiCheckCheckGroupData
import com.nash.android.customer.customerservice.CustomerServiceActivity
import com.nash.android.data.CustomerDataModel
import com.nash.android.data.NashDate
import com.nash.android.util.DateUtil
import com.nash.android.util.convertToString
import kotlinx.android.synthetic.main.customer_new_form_activity.*
import org.parceler.Parcels
import java.util.*

class CustomerNewFormActivity : CoreActivity<CustomerNewFormViewModel>() {
    var isEditMode: Boolean? = null
    override fun onCreateViewModel(): CustomerNewFormViewModel = ViewModelProviders.of(this).get(CustomerNewFormViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_new_form_activity)
        setTitle(R.string.text_new_customer_form_title)
        observeViewModel()
        setupMultiCheckGroups()
        setBackEnabled(true)
        val customerDataModel: CustomerDataModel? = Parcels.unwrap(intent.extras?.getParcelable("customerDataModel"))
        if (customerDataModel != null) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            getViewModel().setCustomerDataModel(customerDataModel)
            setTitle("Customer - ${customerDataModel.customerName}")
            setDataToForm(customerDataModel)
            initToolbarButton()
            buttonSave.text = "Edit"
            isEditMode = false
        }
        setupCalendarDialog()
        buttonSave.setOnClickListener {
            if (isEditMode == null) {
                saveCustomer()
            } else {
                if (isEditMode!!) {
                    saveCustomer()
                } else {
                    isEditMode = true
                    setButtonClickability(false)
                    setFormEnabled(true)
                    buttonSave.text = "Save"
                }
            }
        }
    }

    private fun initToolbarButton() {
        showPrimaryButton()
        showSecondaryButton()
        setButtonClickability(true)
    }

    private fun setButtonClickability(isClickable: Boolean) {
        if (isClickable) {
            setPrimaryButtonImage(R.drawable.ic_customer_white)
            setSecondaryButtonImage(R.drawable.ic_service_gray)
            setPrimaryButtonClick {

            }

            setSecondaryButtonClick {
                navigateToCustomerServiceList()
            }
        } else {
            setPrimaryButtonImage(R.drawable.ic_customer_white)
            setSecondaryButtonImage(R.drawable.ic_service_white)
            setPrimaryButtonClick {

            }

            setSecondaryButtonClick {

            }
        }
    }

    private fun navigateToCustomerServiceList() {
        val bundle = Bundle()
        bundle.putParcelable("customerDataModel", Parcels.wrap(getViewModel().getCustomerData()))
        startActivity(Intent(this, CustomerServiceActivity::class.java).putExtras(bundle))
        finish()
    }

    private fun setDataToForm(customerDataModel: CustomerDataModel) {
        editTextCustomerName.setText(customerDataModel.customerName)
        editTextBirthDate.setText(customerDataModel.customerDateOfBirth.convertToString())
        editTextAddress.setText(customerDataModel.customerAddress)
        editTextEmail.setText(customerDataModel.customerEmail)
        editTextPhoneNumber.setText(customerDataModel.customerPhone)
        checkBoxAgreeTnC.isChecked = true
        hasEyelashBeforeMCG.setChecked(customerDataModel.hasExtensions)
        haveAllergyMCG.setChecked(customerDataModel.hasAllergy)
        wearContactLensMCG.setChecked(customerDataModel.wearContactLens)
        hadEyeSurgeryMCG.setChecked(customerDataModel.hadSurgery)
        knowNashMCG.setChecked(customerDataModel.knowNashFrom)

        hasEyelashBeforeMCG.setAdditionalInfo(customerDataModel.hasExtensionsInfo)
        haveAllergyMCG.setAdditionalInfo(customerDataModel.hasAllergyInfo)
        wearContactLensMCG.setAdditionalInfo(customerDataModel.wearContactLensInfo)
        hadEyeSurgeryMCG.setAdditionalInfo(customerDataModel.hadSurgeryInfo)
        knowNashMCG.setAdditionalInfo(customerDataModel.knowNashFromInfo)

        setFormEnabled(false)

        editTextCustomerName.setTextColor(ContextCompat.getColor(this, R.color.black))
        editTextBirthDate.setTextColor(ContextCompat.getColor(this, R.color.black))
        editTextAddress.setTextColor(ContextCompat.getColor(this, R.color.black))
        editTextEmail.setTextColor(ContextCompat.getColor(this, R.color.black))
        editTextPhoneNumber.setTextColor(ContextCompat.getColor(this, R.color.black))
        editTextCustomerName.setTextColor(ContextCompat.getColor(this, R.color.black))
    }

    private fun setFormEnabled(isEnabled: Boolean) {
        editTextCustomerName.isEnabled = isEnabled
        editTextBirthDate.isEnabled = isEnabled
        editTextAddress.isEnabled = isEnabled
        editTextEmail.isEnabled = isEnabled
        editTextPhoneNumber.isEnabled = isEnabled

        editTextCustomerName.isClickable = isEnabled
        editTextBirthDate.isClickable = isEnabled
        editTextAddress.isClickable = isEnabled
        editTextEmail.isClickable = isEnabled
        editTextPhoneNumber.isClickable = isEnabled

        hasEyelashBeforeMCG.isEnabled = isEnabled
        haveAllergyMCG.isEnabled = isEnabled
        wearContactLensMCG.isEnabled = isEnabled
        hadEyeSurgeryMCG.isEnabled = isEnabled
        knowNashMCG.isEnabled = isEnabled

        checkBoxAgreeTnC.isEnabled = isEnabled
        checkBoxAgreeTnC.isClickable = isEnabled
    }

    private fun observeViewModel() {
        getViewModel().isLoading().observe(this, Observer { observeLoading(it!!) })
        getViewModel().isSuccess().observe(this, Observer {
            if (isEditMode == null) {
                finish()
            } else {
                buttonSave.text = "Edit"
                setFormEnabled(false)
            }
        })
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
            editTextBirthDate.setText(NashDate(p3, p2 + 1, p1).convertToString())
        }, year, month, day)
        datePickerDialog.datePicker.maxDate = calendar.time.time
        datePickerDialog.show()
    }

    private fun saveCustomer() {
        val customerDataModel = if (getViewModel().getCustomerLiveData() != null) getViewModel().getCustomerLiveData() else CustomerDataModel()
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

        customerDataModel!!

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


        customerDataModel.knowNashFrom = knowNashResult.selectedItems
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