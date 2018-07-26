package com.android.nash.service.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.android.nash.data.ServiceDataModel
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import kotlinx.android.synthetic.main.service_item.view.*

class ServiceViewHolder(view: View?):ChildViewHolder(view) {
    private var textViewServiceName: TextView? = itemView?.textViewServiceName
    private var textViewPrice: TextView? = itemView?.textViewPrice
    private var textViewReminder: TextView? = itemView?.textViewReminder
    private var btnEdit: ImageView? = itemView?.buttonEdit
    private var btnDelete: ImageView? = itemView?.buttonDelete

    fun bind(serviceDataModel: ServiceDataModel?, serviceItemCallback: ServiceItemCallback) {
        textViewServiceName?.text = serviceDataModel?.serviceName
        textViewPrice?.text = "Price: ${serviceDataModel?.price.toString()}"
        textViewReminder?.text = "Reminder: ${serviceDataModel?.reminder}"
        btnEdit?.setOnClickListener { serviceItemCallback.onItemEdit(serviceDataModel) }
        btnDelete?.setOnClickListener { serviceItemCallback.onItemDelete(serviceDataModel) }
    }
}