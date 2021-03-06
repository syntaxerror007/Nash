package com.nash.android.service.dialog.list

import android.content.Context
import android.os.Bundle
import com.nash.android.R
import com.nash.android.core.dialog.CoreDialog
import com.nash.android.data.ServiceGroupDataModel
import com.nash.android.service.adapter.ServiceGroupAdapter
import com.nash.android.service.adapter.ServiceGroupListCallback
import com.nash.android.service.adapter.ServiceItemCallback
import kotlinx.android.synthetic.main.service_list_dialog.*

class ServiceListDialog(context: Context, serviceListCallback: ServiceListCallback, serviceGroupDataModels: MutableList<ServiceGroupDataModel>?) : CoreDialog<ServiceListDialogViewModel>(context), ServiceGroupListCallback, ServiceItemCallback {
    private val serviceListCallback = serviceListCallback
    private val serviceGroupDataModels = serviceGroupDataModels
    private val selectedServiceGroupDataModel: MutableMap<String, ServiceGroupDataModel> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.service_list_dialog)
        initDialogAdapter()
        setListener()
    }

    private fun initDialogAdapter() {
        val serviceGroupAdapter = ServiceGroupAdapter(serviceGroupDataModels!!, true)
        serviceGroupAdapter.setGroupListCallback(this)
        serviceGroupAdapter.setServiceItemCallback(this)
        serviceGroupAdapter.setChildClickListener { v, checked, group, childIndex ->
            val selectedChildren = group.selectedChildren
            if (group is ServiceGroupDataModel) {
                val serviceGroupDataModel = ServiceGroupDataModel()
                serviceGroupDataModel.uuid = group.uuid
                serviceGroupDataModel.serviceGroupName = group.serviceGroupName
                selectedChildren.indices
                        .filter { selectedChildren[it] }
                        .map { serviceGroupDataModel.services.add(group.services[it]) }
                selectedServiceGroupDataModel[serviceGroupDataModel.serviceGroupName] = serviceGroupDataModel
            }
        }
        serviceGroupAdapter.expandAllGroups()
        recyclerViewServiceGroup.adapter = serviceGroupAdapter
        serviceGroupAdapter.notifyDataSetChanged()
    }

    private fun setListener() {
        btnClose.setOnClickListener { cancel() }
        btnCancel.setOnClickListener { cancel() }
        btnRegister.setOnClickListener { registerServices() }
    }

    private fun registerServices() {
        serviceListCallback.onFinishServiceClick(selectedServiceGroupDataModel.values.toMutableList())
        dismiss()
    }
}