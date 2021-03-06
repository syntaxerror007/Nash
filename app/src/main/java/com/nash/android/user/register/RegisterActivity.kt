package com.nash.android.user.register

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.nash.android.R
import com.nash.android.core.activity.CoreActivity
import kotlinx.android.synthetic.main.register_activity.*


class RegisterActivity : CoreActivity<RegisterViewModel>(), View.OnClickListener {
    override fun onCreateViewModel(): RegisterViewModel {
        return ViewModelProviders.of(this).get(RegisterViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)
        title = "Register"
        observeViewModel()
        setUserTypeSpinner()
        btnRegister.setOnClickListener(this)

    }

    private fun setUserTypeSpinner() {
        val adapter = ArrayAdapter.createFromResource(this,
                R.array.user_type, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinnerUserType.onItemSelectedListener = object : OnItemSelectedListener {
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//                return
//            }
//
//            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, itemId: Long) {
//                if (position >= 0 && position < adapter.count)
//                    getViewModel().setItemSelected(adapter.getItem(position).toString())
//            }
//        }
//        spinnerUserType.adapter = adapter
    }

    private fun observeViewModel() {

        getViewModel().getUser().observe(this, Observer { observeUser(it) })
        getViewModel().getLoadingState().observe(this, Observer { observeLoadingState(it) })
        getViewModel().getMessage().observe(this, Observer { observeMessage(it) })
        getViewModel().getUsernameError().observe(this, Observer { observeUsernameError(it) })
        getViewModel().getPasswordError().observe(this, Observer { observePasswordError(it) })
        getViewModel().getPhoneError().observe(this, Observer { observePhoneError(it) })
        getViewModel().getNameError().observe(this, Observer { observeNameError(it) })
        getViewModel().getUserTypeError().observe(this, Observer { observeUserTypeError(it) })
        getViewModel().init()
    }

    private fun observeUserTypeError(it: String?) {
//        spinnerUserType.error = it
    }

    private fun observeNameError(it: String?) {
//        editTextName.error = it
    }

    private fun observePhoneError(it: String?) {
//        editTextPhoneNumber.error = it
    }

    private fun observePasswordError(message: String?) {
        editTextPassword.error = message
    }

    private fun observeUsernameError(message: String?) {
        editTextUsername.error = message
    }

    private fun observeMessage(it: String?) {
        Toast.makeText(this, it!!, Toast.LENGTH_LONG).show()
    }

    private fun observeLoadingState(it: Boolean?) {
        loading.isIndeterminate = true
        if (it!! && it) {
            loading.visibility = View.VISIBLE
            btnRegister.visibility = View.GONE
        } else {
            loading.visibility = View.GONE
            btnRegister.visibility = View.VISIBLE
        }
    }

    override fun onClick(view: View?) {
        if (view != null) {
            if (view == btnRegister) {
                val firebaseOptions = FirebaseApp.getInstance()!!.options
//                getViewModel().doRegister(FirebaseApp.initializeApp(this, firebaseOptions, "secondaryFirebaseApp"), editTextUsername.text.toString(), editTextPassword.text.toString(), editTextName.text.toString(), editTextPhoneNumber.text.toString())
            }
        }
    }
}