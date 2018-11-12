package com.android.nash.customer.customersearch

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.android.nash.R
import com.android.nash.core.activity.CoreActivity
import com.android.nash.core.recyclerview.EndlessOnScrollListener
import com.android.nash.customer.customerdetail.CustomerDetailActivity
import com.android.nash.customer.customerservice.CustomerServiceActivity
import com.android.nash.data.CustomerDataModel
import kotlinx.android.synthetic.main.customer_list_activity.*
import org.parceler.Parcels

class CustomerListActivity : CoreActivity<CustomerListViewModel>() {
    private lateinit var customerListAdapter: CustomerListAdapter
    override fun onCreateViewModel(): CustomerListViewModel = ViewModelProviders.of(this).get(CustomerListViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_list_activity)
        setTitle("Customer")
        hideSearchForm()
        setPrimaryButtonClick {
            showSearchForm()
        }
        observeLiveData()
        getViewModel().listenOnTextChanged()
        getViewModel().getAllCustomer()

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

    private fun onCustomerItemClicked(it: CustomerDataModel) {
        val bundle = Bundle()
        bundle.putParcelable("customerDataModel", Parcels.wrap(it))
        if (getViewModel().getUserDataModel().value?.userType.equals("ADMIN")) {
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
    }

    private fun initRecyclerViewCustomer(it: List<CustomerDataModel>) {
        customerListAdapter.updateData(it)
    }
}