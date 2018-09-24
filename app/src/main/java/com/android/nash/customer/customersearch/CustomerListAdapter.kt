package com.android.nash.customer.customersearch

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.android.nash.R
import com.android.nash.data.CustomerDataModel
import com.android.nash.data.CustomerServiceDataModel
import com.android.nash.util.inflate
import com.android.nash.util.setVisible
import kotlinx.android.synthetic.main.layout_customer_item.view.*

class CustomerListAdapter(val data: List<CustomerDataModel> = listOf(), private val onItemClickListener: (CustomerDataModel) -> Unit) : RecyclerView.Adapter<CustomerListAdapter.CustomerListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerListViewHolder = CustomerListViewHolder(parent.inflate(R.layout.layout_customer_item))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: CustomerListViewHolder, position: Int) = holder.bind(data[position], onItemClickListener)

    class CustomerListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(customerServiceDataModel: CustomerDataModel, onItemClickListener: (CustomerDataModel) -> Unit) = with(itemView) {
            textViewCustomerName.text = customerServiceDataModel.customerName
            textViewCustomerEmail.text = customerServiceDataModel.customerEmail
            textViewCustomerPhone.text = customerServiceDataModel.customerPhone
            textViewCustomerService.text = "${customerServiceDataModel.services.size}"
            imageViewNeedToRemind.setVisible(customerServiceDataModel.isNeedToBeReminded)
            setOnClickListener { onItemClickListener(customerServiceDataModel) }
        }
    }
}