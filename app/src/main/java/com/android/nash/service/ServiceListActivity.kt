package com.android.nash.service

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import com.android.nash.R
import com.android.nash.core.activity.CoreActivity
import com.android.nash.data.ServiceGroupDataModel
import com.android.nash.service.adapter.ServiceGroupAdapter
import com.android.nash.service.adapter.ServiceGroupListCallback
import com.android.nash.service.form.ServiceFormDialog
import kotlinx.android.synthetic.main.service_list_activity.*

class ServiceListActivity : CoreActivity<ServiceListViewModel>(), ServiceGroupCallback, ServiceGroupListCallback {

    private lateinit var dialog:ServiceFormDialog

    override fun onCreateService(serviceGroupName: String) {
        getViewModel().insertServiceDialog(serviceGroupName)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.service_list_activity)
        dialog = ServiceFormDialog(this)
        dialog.setServiceGroupCallback(this)
        observe()
        getViewModel().loadAllService()
//        showDialog()
    }

    private fun observe() {
        getViewModel().getInsertServiceGroupError().observe(this, Observer { showErrorMessage(it!!) })
        getViewModel().isInsertServiceGroupSuccess().observe(this, Observer {
            if (it != null && it)
                hideDialog()
        })
        getViewModel().getServiceGroupListLiveData().observe(this, Observer {
            observeServiceGroup(it)
        })
    }

    private fun observeServiceGroup(serviceGroups: List<ServiceGroupDataModel>?) {
        val serviceGroupAdapter = ServiceGroupAdapter(serviceGroups)
        serviceGroupAdapter.setGroupListCallback(this)
        recyclerViewServiceGroup.adapter = serviceGroupAdapter
        serviceGroupAdapter.notifyDataSetChanged()
    }

    private fun hideDialog() {
        dialog.dismiss()
    }


    private fun showDialog() {
        dialog.show()
    }
    private fun showErrorMessage(it: String) {
        Snackbar.make(root, it, Snackbar.LENGTH_LONG).show()
    }

    override fun onCreateViewModel(): ServiceListViewModel {
        return ViewModelProviders.of(this).get(ServiceListViewModel::class.java)
    }

    override fun onEditGroup() {
        Log.d("MO", "Edit Group")
    }

    override fun onAddService() {
        Log.d("MO", "Add Service")
    }
}