package com.android.nash.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.android.nash.R
import com.android.nash.core.activity.CoreActivity
import com.android.nash.location.RegisterLocationActivity
import com.android.nash.login.LoginActivity
import com.android.nash.service.ServiceListActivity
import com.android.nash.user.register.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.home_activity.*

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
        when {
            it.equals("Register User", true) -> goToRegisterActivity()
            it.equals("Register Location", true) -> gotoRegisterLocationActivity()
            it.equals("Log Out", true) -> {
                FirebaseAuth.getInstance().signOut()
                goToLoginActivity()
            }
            it.equals("Services", true) -> goToServiceListActivity()
        }
    }

    private fun goToServiceListActivity() {
        startActivity(Intent(this, ServiceListActivity::class.java))
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