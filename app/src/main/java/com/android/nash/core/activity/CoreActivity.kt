package com.android.nash.core.activity

import android.app.Dialog
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
import android.view.View
import com.android.nash.R
import com.android.nash.core.loading_dialog.LoadingDialog
import com.android.nash.location.list.LocationListActivity
import com.android.nash.location.register.RegisterLocationActivity
import com.android.nash.service.ServiceListActivity
import kotlinx.android.synthetic.main.core_activity.*


abstract class CoreActivity<T : CoreViewModel> : AppCompatActivity(), BaseCoreActivity<T> {
    lateinit var mDrawerToggle: ActionBarDrawerToggle
    lateinit var mDrawerLayout: DrawerLayout
    private var mToolBarNavigationListenerIsRegistered = false
    private lateinit var viewModel: T
    private var backEnabled = false
    private lateinit var loadingDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = onCreateViewModel()
        loadingDialog = LoadingDialog(this)
        initViewModel()
    }

    fun showLoadingDialog() {
        loadingDialog.show()
    }

    fun hideLoadingDialog() {
        loadingDialog.dismiss()
    }
    fun setTitle(title: String) {
        toolbarTitle.text = title
    }

    override fun setTitle(resId: Int) {
        toolbarTitle.setText(resId)
    }

    fun setToolbarRightButtonText(btnText: String) {
        rightButton.text = btnText
    }

    fun setToolbarRightButtonVisible(isVisible: Boolean) {
        rightButton.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setToolbarRightOnClickListener(onClickListener: View.OnClickListener) {
        rightButton.setOnClickListener(onClickListener)
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

    fun hideToolbar() {
        toolbar.visibility = View.GONE
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
        startActivity(Intent(this, LocationListActivity::class.java))
        finish()
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
