package com.android.nash.service

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import com.android.nash.R
import com.android.nash.core.activity.CoreActivity
import com.android.nash.data.ServiceDataModel
import com.android.nash.data.ServiceGroupDataModel
import com.android.nash.service.adapter.ServiceGroupAdapter
import com.android.nash.service.adapter.ServiceGroupListCallback
import com.android.nash.service.adapter.ServiceItemCallback
import com.android.nash.service.form.ServiceCallback
import com.android.nash.service.form.service.ServiceFormDialog
import com.android.nash.service.form.service_group.ServiceGroupFormDialog
import kotlinx.android.synthetic.main.service_list_activity.*

class ServiceListActivity : CoreActivity<ServiceListViewModel>(), ServiceGroupCallback, ServiceGroupListCallback, ServiceItemCallback, ServiceCallback {

    private lateinit var serviceGroupDialog: ServiceGroupFormDialog
    private lateinit var serviceDialog: ServiceFormDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.service_list_activity)
        setTitle("Services")
        btnAddNewServiceGroup.setOnClickListener { showServiceGroupDialog() }
        observe()
        getViewModel().loadAllService()
    }

    private fun observe() {
        getViewModel().getInsertServiceGroupError().observe(this, Observer { showErrorMessage(it!!) })
        getViewModel().isInsertServiceGroupSuccess().observe(this, Observer {
            if (it != null && it)
                hideServiceGroupDialog()
        })
        getViewModel().getServiceGroupListLiveData().observe(this, Observer {
            observeServiceGroup(it!!)
        })

        getViewModel().isInsertServiceSuccess().observe(this, Observer {
            if (it != null && it)
                serviceDialog.dismiss()
        })
        getViewModel().getInsertServiceError().observe(this, Observer { showErrorMessage(it!!) })

    }

    override fun onCreateServiceGroup(serviceGroupName: String) {
        getViewModel().insertServiceGroup(serviceGroupName)
    }

    private fun observeServiceGroup(serviceGroups: MutableList<ServiceGroupDataModel>) {
        val serviceGroupAdapter = ServiceGroupAdapter(serviceGroups, false)
        serviceGroupAdapter.setGroupListCallback(this)
        serviceGroupAdapter.setServiceItemCallback(this)
        serviceGroupAdapter.expandAllGroups()
        recyclerViewServiceGroup.adapter = serviceGroupAdapter
        serviceGroupAdapter.notifyDataSetChanged()
    }

    private fun hideServiceGroupDialog() {
        serviceGroupDialog.dismiss()
    }


    private fun showServiceGroupDialog() {
        serviceGroupDialog = ServiceGroupFormDialog(this, this)
        serviceGroupDialog.show()
    }

    private fun showErrorMessage(it: String) {
        Snackbar.make(root, it, Snackbar.LENGTH_LONG).show()
    }

    override fun onCreateViewModel(): ServiceListViewModel {
        return ViewModelProviders.of(this).get(ServiceListViewModel::class.java)
    }

    override fun onEditGroup(serviceGroupDataModel: ServiceGroupDataModel?, position: Int) {

    }

    override fun onAddService(serviceGroupDataModel: ServiceGroupDataModel?, groupPosition: Int) {
        serviceDialog = ServiceFormDialog(this, this, serviceGroupDataModel, groupPosition, 0)
        serviceDialog.show()
    }

    override fun onItemEdit(serviceGroupDataModel: ServiceGroupDataModel, serviceDataModel: ServiceDataModel?, groupPosition: Int, childPosition: Int) {

    }

    override fun onItemDelete(serviceGroupDataModel: ServiceGroupDataModel, serviceDataModel: ServiceDataModel?, groupPosition: Int, childPosition: Int) {
        getViewModel().removeService(serviceGroupDataModel, serviceDataModel)
    }

    override fun onServiceCreated(serviceDataModel: ServiceDataModel, serviceGroupDataModel: ServiceGroupDataModel?, groupPosition: Int, position: Int) {
        getViewModel().insertService(serviceGroupDataModel= serviceGroupDataModel, serviceDataModel = serviceDataModel)
    }
}