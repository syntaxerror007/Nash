package com.android.nash.service.adapter

import android.view.View
import android.widget.CheckBox
import android.widget.Checkable
import android.widget.ImageView
import android.widget.TextView
import com.android.nash.data.ServiceDataModel
import com.thoughtbot.expandablecheckrecyclerview.viewholders.CheckableChildViewHolder
import kotlinx.android.synthetic.main.service_item.view.*

class ServiceViewHolder(view: View?, isCompactMode: Boolean):CheckableChildViewHolder(view) {
    private var textViewServiceName: TextView = itemView.textViewServiceName
    private var textViewPrice: TextView = itemView.textViewPrice
    private var textViewReminder: TextView = itemView.textViewReminder
    private var btnEdit: ImageView = itemView.buttonEdit
    private var btnDelete: ImageView = itemView.buttonDelete
    private var checkBox: CheckBox = itemView.checkBox
    private var isCompactMode = isCompactMode

    fun bind(serviceDataModel: ServiceDataModel?, serviceItemCallback: ServiceItemCallback) {
        if (isCompactMode) {
            textViewPrice.visibility = View.GONE
            textViewReminder.visibility = View.GONE
            btnEdit.visibility = View.GONE
            btnDelete.visibility = View.GONE
            checkBox.visibility = View.VISIBLE
        }
        textViewServiceName.text = serviceDataModel?.serviceName
        textViewPrice.text = "Price: ${serviceDataModel?.price.toString()}"
        textViewReminder.text = "Reminder: ${serviceDataModel?.reminder}"
        btnEdit.setOnClickListener { serviceItemCallback.onItemEdit(serviceDataModel) }
        btnDelete.setOnClickListener { serviceItemCallback.onItemDelete(serviceDataModel) }
    }

    override fun getCheckable(): Checkable {
        return checkBox
    }
}