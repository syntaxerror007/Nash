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

    override fun onCreateService(serviceGroupName: String) {
        getViewModel().insertServiceGroup(serviceGroupName)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.service_list_activity)
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
            observeServiceGroup(it)
        })
    }

    private fun observeServiceGroup(serviceGroups: List<ServiceGroupDataModel>?) {
        val serviceGroupAdapter = ServiceGroupAdapter(serviceGroups)
        serviceGroupAdapter.setGroupListCallback(this)
        recyclerViewServiceGroup.adapter = serviceGroupAdapter
        serviceGroupAdapter.notifyDataSetChanged()
    }

    private fun hideServiceGroupDialog() {
        serviceGroupDialog.dismiss()
    }


    private fun showServiceGroupDialog() {
        serviceGroupDialog.show()
    }

    private fun showErrorMessage(it: String) {
        Snackbar.make(root, it, Snackbar.LENGTH_LONG).show()
    }

    override fun onCreateViewModel(): ServiceListViewModel {
        return ViewModelProviders.of(this).get(ServiceListViewModel::class.java)
    }

    override fun onEditGroup(serviceGroupDataModel: ServiceGroupDataModel?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAddService(serviceGroupDataModel: ServiceGroupDataModel?) {
        val serviceFormDialog = ServiceFormDialog(this, this, serviceGroupDataModel)
        serviceFormDialog.show()
    }

    override fun onItemEdit(serviceDataModel: ServiceDataModel?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemDelete(serviceDataModel: ServiceDataModel?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onServiceCreated(serviceDataModel: ServiceDataModel, serviceGroupDataModel: ServiceGroupDataModel?) {
        getViewModel().insertService(serviceGroupDataModel= serviceGroupDataModel, serviceDataModel = serviceDataModel)
    }
}