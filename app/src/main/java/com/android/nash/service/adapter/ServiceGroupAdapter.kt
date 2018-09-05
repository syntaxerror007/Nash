package com.android.nash.service.adapter

import android.view.ViewGroup
import com.android.nash.R
import com.android.nash.data.ServiceGroupDataModel
import com.android.nash.util.inflate
import com.android.nash.expandablecheckrecyclerview.CheckableChildRecyclerViewAdapter
import com.android.nash.expandablecheckrecyclerview.models.CheckedExpandableGroup
import com.android.nash.expandablerecyclerview.models.ExpandableGroup

class ServiceGroupAdapter(serviceGroups: MutableList<ServiceGroupDataModel>, isCompactMode: Boolean = false, isPriceEditable: Boolean = false) : CheckableChildRecyclerViewAdapter<ServiceGroupViewHolder, ServiceViewHolder>(serviceGroups) {

    var serviceGroups = serviceGroups
    private var mServiceGroupListCallback: ServiceGroupListCallback? = null
    private var mServiceItemCallback: ServiceItemCallback? = null
    private val isCompactMode = isCompactMode
    private val isPriceEditable = isPriceEditable

    override fun onCreateGroupViewHolder(parent: ViewGroup?, viewType: Int): ServiceGroupViewHolder {
        return ServiceGroupViewHolder(parent?.inflate(R.layout.service_group_item), isCompactMode)
    }

    override fun onBindGroupViewHolder(holder: ServiceGroupViewHolder?, flatPosition: Int, group: ExpandableGroup<*>?) {
        holder?.bind(group as ServiceGroupDataModel, mServiceGroupListCallback, expandableList.getUnflattenedPosition(flatPosition).groupPos)
    }

    override fun onBindCheckChildViewHolder(holder: ServiceViewHolder?, flatPosition: Int, group: CheckedExpandableGroup?, childIndex: Int) {
        val unflattenedPos = expandableList.getUnflattenedPosition(flatPosition)
        holder?.bind((group as ServiceGroupDataModel), group.services[unflattenedPos.childPos], mServiceItemCallback, unflattenedPos.groupPos, unflattenedPos.childPos)
    }

    override fun onCreateCheckChildViewHolder(parent: ViewGroup?, viewType: Int): ServiceViewHolder {
        return ServiceViewHolder(parent?.inflate(R.layout.service_item), isCompactMode, isPriceEditable)
    }

    fun setGroupListCallback(serviceGroupListCallback: ServiceGroupListCallback) {
        this.mServiceGroupListCallback = serviceGroupListCallback
    }

    fun setServiceItemCallback(serviceItemCallback: ServiceItemCallback) {
        this.mServiceItemCallback = serviceItemCallback
    }

    fun expandAllGroups() {
        groups.forEachIndexed { groupIndex, group ->
            if (isGroupExpanded(group).not()) {
                onGroupClick(expandableList.getFlattenedGroupIndex(groupIndex))
            }
        }
    }
}