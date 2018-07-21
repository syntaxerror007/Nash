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


abstract class CoreDialog<T : CoreViewModel> : AppCompatDialog {
    private lateinit var viewModel: T

    constructor(context: Context) : super(context)

    constructor(context: Context, theme: Int) : super(context, theme)

    protected constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener) : super(context, cancelable, cancelListener)

    fun getViewModel():T {
        return viewModel
    }
}
