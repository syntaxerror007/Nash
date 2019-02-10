package com.nash.android.reminder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.nash.android.R
import com.nash.android.data.CustomerServiceDataModel
import com.nash.android.util.convertToString
import com.nash.android.util.inflate
import kotlinx.android.synthetic.main.layout_customer_reminder_item.view.*

class CustomerReminderAdapter(val list: List<CustomerServiceDataModel>, private val onReminded: (CustomerServiceDataModel, Boolean) -> Unit) : RecyclerView.Adapter<CustomerReminderAdapter.CustomerReminderViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerReminderViewHolder = CustomerReminderViewHolder(parent.inflate(R.layout.layout_customer_reminder_item))

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: CustomerReminderViewHolder, position: Int) = holder.bind(list[position], onReminded)

    class CustomerReminderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(customerServiceDataModel: CustomerServiceDataModel, onReminded: (CustomerServiceDataModel, Boolean) -> Unit) = with(itemView) {
            textViewCustomerServiceDate.text = customerServiceDataModel.treatmentDate.convertToString()
            textViewCustomerService.text = customerServiceDataModel.service?.serviceName
            textViewCustomerName.text = customerServiceDataModel.customerDataModel?.customerName
            textViewCustomerPhone.text = customerServiceDataModel.customerDataModel?.customerPhone
            checkBoxHasReminded.isChecked = customerServiceDataModel.hasReminded
            checkBoxHasReminded.setOnCheckedChangeListener { _, isChecked ->
                onReminded.invoke(customerServiceDataModel, isChecked)
            }
        }
    }
}

