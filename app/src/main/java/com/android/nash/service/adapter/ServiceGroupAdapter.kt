package com.android.nash.service.adapter

import android.view.ViewGroup
import com.android.nash.R
import com.android.nash.data.ServiceDataModel
import com.android.nash.data.ServiceGroupDataModel
import com.android.nash.util.inflate
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup

class ServiceGroupAdapter(serviceGroups: List<ServiceGroupDataModel>?) : ExpandableRecyclerViewAdapter<ServiceGroupViewHolder, ServiceViewHolder>(serviceGroups) {
    private lateinit var mServiceGroupListCallback: ServiceGroupListCallback
    private lateinit var mServiceItemCallback: ServiceItemCallback

    override fun onCreateGroupViewHolder(parent: ViewGroup?, viewType: Int): ServiceGroupViewHolder {
        return ServiceGroupViewHolder(parent?.inflate(R.layout.service_group_item))

    }

    override fun onCreateChildViewHolder(parent: ViewGroup?, viewType: Int): ServiceViewHolder {
        return ServiceViewHolder(parent?.inflate(R.layout.service_item))
    }

    override fun onBindChildViewHolder(holder: ServiceViewHolder?, flatPosition: Int, group: ExpandableGroup<*>?, childIndex: Int) {
        holder?.bind((group as ServiceGroupDataModel).services[childIndex], mServiceItemCallback)
    }

    override fun onBindGroupViewHolder(holder: ServiceGroupViewHolder?, flatPosition: Int, group: ExpandableGroup<*>?) {
        holder?.bind(group as ServiceGroupDataModel, mServiceGroupListCallback, flatPosition)
    }

    fun setGroupListCallback(serviceGroupListCallback: ServiceGroupListCallback) {
        this.mServiceGroupListCallback = serviceGroupListCallback
    }

    fun setServiceItemCallback(serviceItemCallback: ServiceItemCallback) {
        this.mServiceItemCallback = serviceItemCallback
    }

}