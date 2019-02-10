package com.nash.android.service.adapter

import android.view.View
import android.widget.Checkable
import android.widget.CheckedTextView
import android.widget.ImageView
import android.widget.TextView
import com.nash.android.data.ServiceDataModel
import com.nash.android.data.ServiceGroupDataModel
import com.nash.android.expandablecheckrecyclerview.viewholders.CheckableChildViewHolder
import kotlinx.android.synthetic.main.service_item.view.*

class ServiceViewHolder(view: View?, isCompactMode: Boolean, isPriceEditable: Boolean) : CheckableChildViewHolder(view) {
    private var textViewServiceName: CheckedTextView = itemView.textViewServiceName
    private var textViewReminder: TextView = itemView.textViewReminder
    private var btnEdit: ImageView = itemView.buttonEdit
    private var btnDelete: ImageView = itemView.buttonDelete
    private var isCompactMode = isCompactMode
    private var isPriceEditable = isPriceEditable

    fun bind(serviceGroupDataModel: ServiceGroupDataModel, serviceDataModel: ServiceDataModel?, serviceItemCallback: ServiceItemCallback?, groupPosition: Int, childPosition: Int) {
        if (isCompactMode) {
            if (!isPriceEditable) {
                textViewReminder.visibility = View.GONE
                btnEdit.visibility = View.GONE
                btnDelete.visibility = View.GONE
            } else {
                btnDelete.visibility = View.GONE
            }
        }
        if (!isCompactMode || isPriceEditable) {
            textViewServiceName.setCompoundDrawables(null, null, null, null)
        }
        textViewServiceName.text = serviceDataModel?.serviceName
        textViewReminder.text = "Reminder: ${serviceDataModel?.reminder}"
        btnEdit.setOnClickListener { serviceItemCallback?.onItemEdit(serviceGroupDataModel, serviceDataModel, groupPosition, childPosition) }
        btnDelete.setOnClickListener { serviceItemCallback?.onItemDelete(serviceGroupDataModel, serviceDataModel, groupPosition, childPosition) }
    }

    override fun getCheckable(): Checkable {
        return textViewServiceName
    }
}