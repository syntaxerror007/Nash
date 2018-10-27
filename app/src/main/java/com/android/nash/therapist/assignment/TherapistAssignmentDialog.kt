package com.android.nash.therapist.assignment

import android.content.Context
import android.os.Bundle
import com.android.nash.R
import com.android.nash.core.CoreViewModel
import com.android.nash.core.dialog.CoreDialog
import com.android.nash.data.ServiceDataModel
import com.android.nash.data.TherapistDataModel
import kotlinx.android.synthetic.main.therapist_assignment_dialog.*

class TherapistAssignmentDialog(context: Context, serviceDataModel: ServiceDataModel?, listTherapist: List<TherapistDataModel>, therapistAssignmentCallback: TherapistAssignmentCallback) : CoreDialog<CoreViewModel>(context) {
    private val listTherapist = listTherapist
    private val therapistAssignmentCallback = therapistAssignmentCallback
    private val serviceDataModel = serviceDataModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.therapist_assignment_dialog)
        initDialogSize()
        setData()
        btnCancel.setOnClickListener { cancel() }
        btnClose.setOnClickListener { cancel() }
        btnRegister.setOnClickListener {
            therapistAssignmentCallback.onTherapistAssigned(serviceDataModel, therapistSelector.selectedIndicies.map { listTherapist[it] })
        }
    }

    private fun setData() {
        therapistSelector.setItems(listTherapist.mapNotNull { it.therapistName })
        val selectedAssignment = listTherapist.filter { it.assignmentSet.contains(serviceDataModel?.uuid) }.map { it.therapistName }.toList()
        if (selectedAssignment.isNotEmpty())
            therapistSelector.setSelection(selectedAssignment)
    }
}