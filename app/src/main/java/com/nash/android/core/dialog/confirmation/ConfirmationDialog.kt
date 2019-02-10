package com.nash.android.core.dialog.confirmation

import android.content.Context
import android.os.Bundle
import com.nash.android.R
import com.nash.android.core.dialog.CoreDialog
import com.nash.android.util.StringUtil
import kotlinx.android.synthetic.main.layout_confirmation_dialog.*


class ConfirmationDialog(context: Context) : CoreDialog<ConfirmationDialogViewModel>(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_confirmation_dialog)
//        initDialogSize()
        btnNo.text = getViewModel().noMessage
        btnYes.text = getViewModel().yesMessage
        dialogTitle.text = getViewModel().dialogTitle
        dialogText.text = StringUtil.fromHtml(getViewModel().dialogMessage)
        btnNo.setOnClickListener { getViewModel().onNoClicked.invoke() }
        btnYes.setOnClickListener { getViewModel().onYesClicked.invoke() }
    }

    fun setData(viewModel: ConfirmationDialogViewModel) {
        setViewModel(viewModel)
    }
}