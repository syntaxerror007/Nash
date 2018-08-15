package com.android.nash.service.dialog

import android.content.Context
import android.os.Bundle
import com.android.nash.R
import com.android.nash.core.dialog.CoreDialog
import com.android.nash.data.ServiceDataModel
import com.android.nash.data.ServiceGroupDataModel
import com.android.nash.service.adapter.ServiceGroupAdapter
import com.android.nash.service.adapter.ServiceGroupListCallback
import com.android.nash.service.adapter.ServiceItemCallback
import kotlinx.android.synthetic.main.service_list_dialog.*

class ServiceListDialog(context: Context, serviceGroupDataModels: List<ServiceGroupDataModel>?) : CoreDialog<ServiceListDialogViewModel>(context), ServiceGroupListCallback, ServiceItemCallback {

    private val serviceGroupDataModels = serviceGroupDataModels
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.service_list_dialog)
        initDialogSize()
        initDialogAdapter()
        setListener()
    }

    private fun initDialogAdapter() {
        val serviceGroupAdapter = ServiceGroupAdapter(serviceGroupDataModels, true)
        serviceGroupAdapter.setGroupListCallback(this)
        serviceGroupAdapter.setServiceItemCallback(this)
        serviceGroupAdapter.expandAllGroups()
        recyclerViewServiceGroup.adapter = serviceGroupAdapter
        serviceGroupAdapter.notifyDataSetChanged()
    }

    private fun setListener() {
        btnClose.setOnClickListener { dismiss() }
        btnCancel.setOnClickListener { cancel() }
        btnRegister.setOnClickListener { registerServices() }
    }

    private fun registerServices() {

    }
    override fun onItemEdit(serviceDataModel: ServiceDataModel?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemDelete(serviceDataModel: ServiceDataModel?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onEditGroup(serviceGroupDataModel: ServiceGroupDataModel?, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAddService(serviceGroupDataModel: ServiceGroupDataModel?, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}