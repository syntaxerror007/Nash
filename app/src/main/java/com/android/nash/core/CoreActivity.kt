package com.android.nash.core

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.android.nash.login.LoginActivity
import com.google.firebase.auth.FirebaseUser

abstract class CoreActivity<T : CoreViewModel> : AppCompatActivity(), BaseCoreActivity<T>{
    private lateinit var viewModel: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = onCreateViewModel()
        initViewModel()
    }

    fun getViewModel():T {
        return viewModel
    }

    override fun initViewModel() {
        viewModel.getUser().observe(this, Observer { observeUser(it) })
    }

    override fun observeUser(it: FirebaseUser?) {
        if (it == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
