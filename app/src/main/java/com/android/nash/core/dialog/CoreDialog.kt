package com.android.nash.core.dialog

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDialog
import com.android.nash.core.CoreViewModel
import com.android.nash.core.activity.BaseCoreActivity
import com.android.nash.login.LoginActivity
import com.google.firebase.auth.FirebaseUser


abstract class CoreDialog<T : CoreViewModel>(context: Context) : AppCompatDialog(context) {
    private lateinit var viewModel: T

    fun getViewModel():T {
        return viewModel
    }


    fun initDialogSize() {
        val metrics = context.resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        this.window.setLayout(6 * width / 7, 4 * height / 5)
    }
}
