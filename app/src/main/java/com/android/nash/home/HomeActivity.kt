package com.android.nash.home

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.android.nash.R
import com.android.nash.core.CoreActivity
import com.android.nash.user.register.RegisterActivity
import kotlinx.android.synthetic.main.home_activity.*
import java.util.*

class HomeActivity : CoreActivity<HomeViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        recyclerViewMenu.layoutManager = LinearLayoutManager(this)
        recyclerViewMenu.adapter = HomeMenuAdapter(Collections.singletonList("Register"), this, onClickListener = { onItemClick(it) })
    }

    private fun onItemClick(it: String) {
        if (it.equals("Register", true)) {
            goToRegisterActivity()
        }
    }

    private fun goToRegisterActivity() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    override fun onCreateViewModel(): HomeViewModel {
        return ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }
}