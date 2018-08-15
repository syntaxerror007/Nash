package com.android.nash.service.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.android.nash.data.ServiceGroupDataModel
import com.android.nash.service.ServiceGroupCallback
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import kotlinx.android.synthetic.main.service_group_item.view.*

class ServiceGroupViewHolder(itemView: View?, isCompactMode: Boolean) : GroupViewHolder(itemView) {
    private var buttonEdit: ImageView? = itemView?.editGroupBtn
    private var titleTextView: TextView? = itemView?.groupTitleTextView
    private var chevron: ImageView? = itemView?.chevron
    private var buttonAddService: TextView? = itemView?.addServiceTextView
    private var isExpanding = true
    private var isCompactMode = isCompactMode

    fun bind(serviceGroupDataModel: ServiceGroupDataModel?, serviceGroupListCallback: ServiceGroupListCallback, position: Int) {
        if (isCompactMode) {
            buttonEdit?.visibility = View.GONE
            buttonAddService?.visibility = View.GONE
        }
        buttonEdit?.setOnClickListener { serviceGroupListCallback.onEditGroup(serviceGroupDataModel, position) }
        buttonAddService?.setOnClickListener { serviceGroupListCallback.onAddService(serviceGroupDataModel, position) }
        titleTextView?.text = serviceGroupDataModel?.serviceGroupName
        chevron?.setOnClickListener {
            if (isExpanding)
                expand()
            else
                collapse()
            isExpanding = !isExpanding
        }
    }
}