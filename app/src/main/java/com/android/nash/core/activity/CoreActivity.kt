package com.android.nash.core.activity

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import com.android.nash.core.CoreViewModel
import com.android.nash.login.LoginActivity
import com.google.firebase.auth.FirebaseUser
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import com.android.nash.R
import com.android.nash.location.RegisterLocationActivity
import com.android.nash.service.ServiceListActivity
import kotlinx.android.synthetic.main.core_activity.*


abstract class CoreActivity<T : CoreViewModel> : AppCompatActivity(), BaseCoreActivity<T> {
    lateinit var mDrawerToggle: ActionBarDrawerToggle
    lateinit var mDrawerLayout: DrawerLayout
    private var mToolBarNavigationListenerIsRegistered = false
    private lateinit var viewModel: T
    private var backEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = onCreateViewModel()
        initViewModel()
    }

    fun setTitle(title: String) {
        toolbarTitle.text = title
    }

    override fun setTitle(resId: Int) {
        toolbarTitle.setText(resId)
    }

    fun setBackEnabled(isBackEnabled: Boolean) {
        if (isBackEnabled) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_material)

        } else {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        }
        backEnabled = isBackEnabled
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        mDrawerLayout = drawerLayout
        mDrawerToggle = ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close)
        setBackEnabled(false)
        setDrawerItemClickListener()

        drawerLayout.addDrawerListener(mDrawerToggle)
        mDrawerToggle.syncState()
    }

    fun setDrawerItemClickListener() {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_landing_page -> { gotoLandingPage() }
                R.id.menu_location -> { gotoLocationPage() }
                R.id.menu_customer -> { gotoCustomerPage() }
                R.id.menu_service -> { gotoServicePage() }
                R.id.menu_setting -> { gotoSettingPage() }
            }
            menuItem.isChecked = true
            mDrawerLayout.closeDrawers()
            true
        }
    }

    private fun gotoSettingPage() {

    }

    private fun gotoServicePage() {
        startActivity(Intent(this, ServiceListActivity::class.java))
        finish()
    }

    private fun gotoCustomerPage() {

    }

    private fun gotoLocationPage() {
        startActivity(Intent(this, RegisterLocationActivity::class.java))
        finish()
    }

    private fun gotoLandingPage() {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                if (backEnabled)
                    onBackPressed()
                else
                    mDrawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun setContentView(resId: Int) {
        super.setContentView(R.layout.core_activity)
        layoutInflater.inflate(resId, coreContainer, true)
        setupToolbar()
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
