package com.android.nash.core.dialog.loading

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatDialog
import com.android.nash.R

class LoadingDialog(context: Context) : AppCompatDialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_dialog)
        setCancelable(false)
    }
}