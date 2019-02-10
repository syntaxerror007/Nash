package com.nash.android.setting

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.widget.Toast
import com.nash.android.R
import com.nash.android.core.activity.CoreActivity
import kotlinx.android.synthetic.main.setting_activity.*

class SettingActivity : CoreActivity<SettingViewModel>() {
    override fun onCreateViewModel(): SettingViewModel = ViewModelProviders.of(this).get(SettingViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting_activity)
        setTitle("Change Password")
        setDrawerItemSelected(R.id.menu_setting)
        observeViewModel()
        btnChangePassword.setOnClickListener {
            if (passwordInputtedCorrect()) {
                val newPassword = editTextNewPassword.text.toString()
                getViewModel().changePassword(newPassword)
            } else {
                editTextRepeatNewPassword.error = "Please put same password"
            }
        }
    }

    private fun observeViewModel() {
        getViewModel().getMessageLiveData().observe(this, Observer { showMessage(it) })
        getViewModel().isLoading().observe(this, Observer { processLoading(it) })
        getViewModel().getUser().observe(this, Observer {
            hideLoadingDialog()
        })
    }

    private fun processLoading(it: Boolean?) = if (it != null && it) showLoadingDialog() else hideLoadingDialog()

    private fun showMessage(it: String?) = Toast.makeText(this, it, Toast.LENGTH_LONG).show()


    private fun passwordInputtedCorrect(): Boolean = editTextNewPassword.text.toString() == editTextRepeatNewPassword.text.toString()

}