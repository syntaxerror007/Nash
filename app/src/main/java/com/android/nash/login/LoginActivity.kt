package com.android.nash.login

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.nash.R
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.login_activity.*

class LoginActivity : AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        viewModel.getUser().observe(this, Observer { observeUser(it) })
    }

    private fun observeUser(user: FirebaseUser?) {
        if (user != null) {
            Toast.makeText(this, user.email, Toast.LENGTH_LONG).show()
        } else {
            editTextUsername.setText("OI")
            Toast.makeText(this, "OI", Toast.LENGTH_LONG).show()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.init()
    }

    override fun onResume() {
        super.onResume()

    }
}
