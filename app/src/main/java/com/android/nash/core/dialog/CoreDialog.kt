package com.android.nash.core.dialog

import android.content.Context
import android.support.v7.app.AppCompatDialog
import com.android.nash.core.CoreViewModel


abstract class CoreDialog<T : CoreViewModel>(context: Context) : AppCompatDialog(context) {
    private lateinit var viewModel: T

    fun getViewModel():T {
        return viewModel
    }

    fun setViewModel(viewModel: T) {
        this.viewModel = viewModel
    }

    fun initDialogSize() {
        val metrics = context.resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        this.window?.setLayout(6 * width / 7, 4 * height / 5)
    }
}
