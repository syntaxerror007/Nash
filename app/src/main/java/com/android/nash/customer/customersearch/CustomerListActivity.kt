package com.android.nash.customer.customersearch

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.android.nash.R
import com.android.nash.backgroundservice.CustomerDownloadService
import com.android.nash.core.activity.CoreActivity
import com.android.nash.core.recyclerview.EndlessOnScrollListener
import com.android.nash.customer.customerdetail.CustomerDetailActivity
import com.android.nash.customer.customerservice.CustomerServiceActivity
import com.android.nash.data.CustomerDataModel
import com.android.nash.util.ADMIN_TYPE
import kotlinx.android.synthetic.main.customer_list_activity.*
import org.parceler.Parcels

class CustomerListActivity : CoreActivity<CustomerListViewModel>() {
    private lateinit var customerListAdapter: CustomerListAdapter
    override fun onCreateViewModel(): CustomerListViewModel = ViewModelProviders.of(this).get(CustomerListViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_list_activity)
        setTitle("Customer")

        setDrawerItemSelected(R.id.menu_customer)
        hideSearchForm()
        setPrimaryButtonClick {
            showSearchForm()
        }
        observeLiveData()
        getViewModel().listenOnTextChanged()
        getViewModel().getAllCustomer()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val mLayoutManager = LinearLayoutManager(this)
        recyclerViewCustomer.layoutManager = mLayoutManager
        recyclerViewCustomer.isNestedScrollingEnabled = true
        recyclerViewCustomer.addOnScrollListener(object : EndlessOnScrollListener(mLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                getViewModel().loadMore()
            }
        })
        customerListAdapter = CustomerListAdapter(mutableListOf()) { onCustomerItemClicked(it) }
        recyclerViewCustomer.adapter = customerListAdapter
    }

    private fun downloadAsCSV() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            }
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    0x123)
        } else {

            Intent(this, CustomerDownloadService::class.java).also {
                startService(it)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            0x123 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    downloadAsCSV()
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }

    private fun onCustomerItemClicked(it: CustomerDataModel) {
        val bundle = Bundle()
        bundle.putParcelable("customerDataModel", Parcels.wrap(it))
        if (getViewModel().getUserDataModel().value?.userType.equals(ADMIN_TYPE)) {
            startActivity(Intent(this, CustomerDetailActivity::class.java).putExtras(bundle))
        } else {
            startActivity(Intent(this, CustomerServiceActivity::class.java).putExtras(bundle))
        }
    }

    private fun observeLiveData() {
        getViewModel().getCustomerLiveData().observe(this, Observer { initRecyclerViewCustomer(it!!) })
        getViewModel().isLoadingLiveData().observe(this, Observer {
            if (it != null && it) {
                showLoadingDialog()
            } else {
                hideLoadingDialog()
            }
        })

        getViewModel().getUserDataModel().observe(this, Observer {
            requestedOrientation = if (it != null && it.userType == ADMIN_TYPE) {
                showPrimaryButton()
                setPrimaryButtonClick { downloadAsCSV() }
                setPrimaryButtonImage(R.drawable.ic_export_excel)
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            } else {
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
        })
    }

    private fun initRecyclerViewCustomer(it: List<CustomerDataModel>) {
        customerListAdapter.updateData(it)
    }
}