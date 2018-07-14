package com.android.nash.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.android.nash.R
import com.android.nash.core.CoreActivity
import com.android.nash.data.UserDataModel
import com.android.nash.location.RegisterLocationActivity
import com.android.nash.login.LoginActivity
import com.android.nash.provider.LocationProvider
import com.android.nash.user.register.RegisterActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.home_activity.*
import java.util.Collections

class HomeActivity : CoreActivity<HomeViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        getViewModel().getUserMenu().observe(this, Observer { observeUserMenu(it) })
    }

    private fun observeUserMenu(userMenus: List<String>?) {
        if (userMenus != null) {
            recyclerViewMenu.layoutManager = LinearLayoutManager(this)
            recyclerViewMenu.adapter = HomeMenuAdapter(userMenus, this, onClickListener = { onItemClick(it) })
        }
    }

    private fun onItemClick(it: String) {
        if (it.equals("Register User", true)) {
            goToRegisterActivity()
        } else if (it.equals("Register Location", true)) {
            gotoRegisterLocationActivity()
        } else if (it.equals("Log Out", true)) {
            FirebaseAuth.getInstance().signOut()
            goToLoginActivity()
        }
    }

    private fun gotoRegisterLocationActivity() {
        startActivity(Intent(this, RegisterLocationActivity::class.java))
    }

    private fun goToLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun goToRegisterActivity() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    override fun onCreateViewModel(): HomeViewModel {
        return ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }
}