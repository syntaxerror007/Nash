package com.android.nash.core.loading_dialog

import android.content.Context
import android.os.Bundle
import com.android.nash.R
import com.android.nash.core.CoreViewModel
import com.android.nash.core.dialog.CoreDialog

class LoadingDialog(context: Context) : CoreDialog<CoreViewModel>(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_dialog)
    }
}