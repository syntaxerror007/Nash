package com.android.nash.newcustomer

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.android.nash.core.activity.CoreActivity
import com.android.nash.R
import com.android.nash.core.yesnocheckbox.MultiCheckCheckGroupData
import kotlinx.android.synthetic.main.customer_new_form_activity.*

class CustomerNewFormActivity : CoreActivity<CustomerNewFormViewModel>() {
    override fun onCreateViewModel(): CustomerNewFormViewModel = ViewModelProviders.of(this).get(CustomerNewFormViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_new_form_activity)
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