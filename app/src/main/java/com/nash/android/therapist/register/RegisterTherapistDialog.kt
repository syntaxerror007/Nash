package com.nash.android.therapist.register

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import com.nash.android.R
import com.nash.android.core.dialog.CoreDialog
import com.nash.android.data.NashDate
import com.nash.android.data.TherapistDataModel
import com.nash.android.util.DateUtil
import com.nash.android.util.convertToString
import com.nash.android.util.dismissKeyboard
import kotlinx.android.synthetic.main.register_therapist_dialog.*
import java.util.*


class RegisterTherapistDialog(context: Context, therapistRegisterCallback: TherapistRegisterCallback, private var therapistDataModel: TherapistDataModel? = null, private var position: Int = 0) : CoreDialog<RegisterTherapistViewModel>(context) {
    private var therapistCallback: TherapistRegisterCallback = therapistRegisterCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_therapist_dialog)
        if (therapistDataModel != null) {
            editTextTherapistName.setText(therapistDataModel?.therapistName)
            editTextPhoneNumber.setText(therapistDataModel?.phoneNumber)
            editTextWorkSince.setText(therapistDataModel?.workSince?.convertToString())
        }
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
            editTextWorkSince.setText(NashDate(p3, p2 + 1, p1).convertToString())
        }, year, month, day)
        datePickerDialog.datePicker.maxDate = calendar.time.time
        datePickerDialog.show()
    }

    private fun registerTherapist() {
        val therapistName = editTextTherapistName.text.toString()
        val phoneNumber = editTextPhoneNumber.text.toString()
        val workSince = editTextWorkSince.text.toString()

        if (isDataValid(therapistName, phoneNumber, workSince)) {
            if (therapistDataModel != null) {
                therapistDataModel?.therapistName = therapistName
                therapistDataModel?.phoneNumber = phoneNumber
                therapistDataModel?.workSince = DateUtil.convertShownDateToNashDate(workSince)
                therapistCallback.onTherapistRegister(therapistDataModel!!, position)
                dismissKeyboard()
                dismiss()

            } else {
                val therapistDataModel = TherapistDataModel(therapistName = therapistName, phoneNumber = phoneNumber, workSince = DateUtil.convertShownDateToNashDate(workSince), job = 0)
                therapistCallback.onTherapistRegister(therapistDataModel, position)
                dismissKeyboard()
                dismiss()
            }

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