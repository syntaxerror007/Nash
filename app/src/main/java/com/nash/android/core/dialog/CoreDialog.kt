package com.nash.android.core.dialog

import android.content.Context
import android.support.v7.app.AppCompatDialog
import android.widget.LinearLayout
import com.nash.android.core.CoreViewModel


abstract class CoreDialog<T : CoreViewModel>(context: Context) : AppCompatDialog(context) {
    private lateinit var viewModel: T

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        initDialogSize()
    }

    fun getViewModel(): T {
        return viewModel
    }

    fun setViewModel(viewModel: T) {
        this.viewModel = viewModel
    }

    private fun initDialogSize() {
        val metrics = context.resources.displayMetrics
        val width = metrics.widthPixels

        this.window?.setLayout(6 * width / 7, LinearLayout.LayoutParams.WRAP_CONTENT)
    }
}
