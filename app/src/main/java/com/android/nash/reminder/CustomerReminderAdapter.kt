package com.android.nash.reminder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.android.nash.R
import com.android.nash.data.CustomerServiceDataModel
import com.android.nash.util.convertToString
import com.android.nash.util.inflate
import kotlinx.android.synthetic.main.layout_customer_reminder_item.view.*

class CustomerReminderAdapter(val list: List<CustomerServiceDataModel>) : RecyclerView.Adapter<CustomerReminderAdapter.CustomerReminderViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerReminderViewHolder = CustomerReminderViewHolder(parent.inflate(R.layout.layout_customer_reminder_item))

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: CustomerReminderViewHolder, position: Int) = holder.bind(list[position])

    class CustomerReminderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(customerServiceDataModel: CustomerServiceDataModel) = with(itemView) {
            textViewCustomerServiceDate.text = customerServiceDataModel.treatmentDate.convertToString()
            textViewCustomerService.text = customerServiceDataModel.service.serviceName
            textViewCustomerName.text = customerServiceDataModel.customerUUID
            textViewCustomerPhone.text = customerServiceDataModel.customerUUID
            checkBoxHasReminded.isChecked = customerServiceDataModel.hasReminded
        }
    }
}

