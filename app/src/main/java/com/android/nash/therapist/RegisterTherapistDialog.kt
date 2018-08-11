package com.android.nash.therapist

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import com.android.nash.R
import com.android.nash.core.dialog.CoreDialog
import com.android.nash.data.NashDate
import com.android.nash.data.TherapistDataModel
import com.android.nash.util.DateUtil
import com.android.nash.util.convertToString
import kotlinx.android.synthetic.main.register_therapist_dialog.*
import java.util.*



class RegisterTherapistDialog(context: Context, therapistRegisterCallback: TherapistRegisterCallback) : CoreDialog<RegisterTherapistViewModel>(context) {
    private var therapistCallback: TherapistRegisterCallback = therapistRegisterCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_therapist_dialog)
        initDialogSize()
        setListener()
    }

    private fun setListener() {
        btnClose.setOnClickListener { dismiss() }
        btnCancel.setOnClickListener { cancel() }
        btnRegister.setOnClickListener { registerTherapist() }
        editTextWorkSince.setOnClickListener { openCalendarDialog() }
    }

    private fun openCalendarDialog() {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        val datePickerDialog = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { p0, p1, p2, p3 ->
            editTextWorkSince.setText(NashDate(p3, p2+1, p1).convertToString())
        }, year, month, day)
        datePickerDialog.datePicker.maxDate = calendar.time.time
        datePickerDialog.show()
    }

    private fun registerTherapist() {
        val therapistName = editTextTherapistName.text.toString()
        val phoneNumber = editTextPhoneNumber.text.toString()
        val workSince = editTextWorkSince.text.toString()

        if (isDataValid(therapistName, phoneNumber, workSince)) {
            val therapistDataModel = TherapistDataModel(therapistName = therapistName, phoneNumber = phoneNumber, workSince = DateUtil.convertShownDateToNashDate(workSince), job = 0)
            therapistCallback.onTherapistRegister(therapistDataModel)
            dismiss()
        }

    }

    private fun isDataValid(therapistName: String, phoneNumber: String, workSince: String): Boolean {
        if (therapistName.isBlank()) {
            editTextTherapistName.error = "Please fill therapist name"
            return false
        }
        if (phoneNumber.isBlank()) {
            editTextPhoneNumber.error = "Please fill phone number"
            return false
        }
        if (workSince.isBlank()) {
            editTextWorkSince.error = "Please fill work since"
            return false
        }
        editTextTherapistName.error = null
        editTextPhoneNumber.error = null
        editTextWorkSince.error = null
        return true
    }
}