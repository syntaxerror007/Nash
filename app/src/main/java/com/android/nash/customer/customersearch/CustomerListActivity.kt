package com.android.nash.customer.customersearch

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import com.android.nash.R
import com.android.nash.core.activity.CoreActivity
import com.android.nash.core.recyclerview.EndlessOnScrollListener
import com.android.nash.customer.customerservice.CustomerServiceActivity
import com.android.nash.data.CustomerDataModel
import kotlinx.android.synthetic.main.customer_list_activity.*
import org.parceler.Parcels

class CustomerListActivity : CoreActivity<CustomerListViewModel>() {
    override fun onCreateViewModel(): CustomerListViewModel = ViewModelProviders.of(this).get(CustomerListViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_list_activity)
        setTitle("Customer")
        hideSearchForm()
        button.setOnClickListener {
            getViewModel().loadMore()
        }
        setPrimaryButtonClick {
            showSearchForm()
        }
        observeLiveData()
        getViewModel().listenOnTextChanged()
        getViewModel().getAllCustomer()
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
        recyclerViewCustomer.adapter = CustomerListAdapter(it) {
            val bundle = Bundle()
            bundle.putParcelable("customerDataModel", Parcels.wrap(it))
            startActivity(Intent(this, CustomerServiceActivity::class.java).putExtras(bundle))
        }

        recyclerViewCustomer.addOnScrollListener(object : EndlessOnScrollListener() {
            override fun onLoadMore() {
//                getViewModel().loadMore()
            }
        })
    }
}