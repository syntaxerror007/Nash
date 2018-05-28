package com.android.nash.user.register

import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.android.nash.R
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.register_activity.*

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewModel: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)
        viewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        viewModel.getUser().observe(this, Observer { observeUser(it) })
        viewModel.getLoadingState().observe(this, Observer { observeLoadingState(it) })
        viewModel.getMessage().observe(this, Observer { observeMessage(it) })
        viewModel.init()

        btnRegister.setOnClickListener(this)
    }

    private fun observeMessage(it: String?) {
        Toast.makeText(this, it!!, Toast.LENGTH_LONG).show()
    }

    private fun observeLoadingState(it: Boolean?) {
        loading.isIndeterminate = true
        if (it!! && it) {
            loading.bringToFront()
            loading.visibility = View.VISIBLE
        } else {
            loading.visibility = View.GONE
        }
    }

    override fun onClick(view: View?) {
        if (view != null) {
            if (view == btnRegister) {
                viewModel.doRegister(editTextUsername.text.toString(), editTextPassword.text.toString(), editTextName.text.toString(), editTextPhoneNumber.text.toString())
            }
        }
    }

    private fun observeUser(it: FirebaseUser?) {
        if (it == null) {
            //TODO move to Login Activity
        }
    }
}