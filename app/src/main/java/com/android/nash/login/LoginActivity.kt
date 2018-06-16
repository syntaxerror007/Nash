package com.android.nash.login

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.android.nash.R
import com.android.nash.core.CoreActivity
import com.android.nash.home.HomeActivity
import com.android.nash.user.register.RegisterActivity
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.login_activity.*

class LoginActivity : CoreActivity<LoginViewModel>(), View.OnClickListener {
    override fun onCreateViewModel(): LoginViewModel {
        return ViewModelProviders.of(this).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        getViewModel().getLoadingState().observe(this, Observer { observeLoadingState(it) })
        getViewModel().getPasswordError().observe(this, Observer { observePasswordError(it) })
        getViewModel().getUsernameError().observe(this, Observer { observeUsernameError(it) })
        getViewModel().getSuccessLogin().observe(this, Observer { observeSuccessLogin(it) })
        btnLogin.setOnClickListener(this)
    }

    private fun observeSuccessLogin(isLoginSuccess: Boolean?) {
        if (isLoginSuccess!! && isLoginSuccess) {
            goToHomeActivity()
        }
    }

    private fun observeUsernameError(usernameErrorMessage: String?) {
        if (!usernameErrorMessage.isNullOrBlank()) {
            editTextUsername.error = usernameErrorMessage
        } else {
            editTextUsername.error = null
        }
    }

    private fun observePasswordError(passwordErrorMessage: String?) {
        if (!passwordErrorMessage.isNullOrBlank()) {
            editTextPassword.error = passwordErrorMessage
        } else {
            editTextPassword.error = null
        }
    }

    private fun observeLoadingState(it: Boolean?) {
        if (it!! && it) {
            loading.visibility = View.VISIBLE
            btnLogin.visibility = View.GONE
        } else {
            btnLogin.visibility = View.VISIBLE
            loading.visibility = View.GONE
        }
    }

    override fun onClick(view: View?) {
        getViewModel().doLogin(editTextUsername.text.toString(), editTextPassword.text.toString())
    }

    override fun observeUser(it: FirebaseUser?) {
        if (it != null) {
            goToHomeActivity()
        }
    }

    private fun goToHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
