package com.nash.android.customer.customersearch

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.nash.android.R
import com.nash.android.data.CustomerDataModel
import com.nash.android.util.inflate
import com.nash.android.util.setVisible
import kotlinx.android.synthetic.main.layout_customer_item.view.*

class CustomerListAdapter(val data: MutableList<CustomerDataModel> = mutableListOf(), private val onItemClickListener: (CustomerDataModel) -> Unit, private val onDeleteClicked: (CustomerDataModel) -> Unit, private val isAdmin: Boolean) : RecyclerView.Adapter<CustomerListAdapter.CustomerListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerListViewHolder = CustomerListViewHolder(parent.inflate(R.layout.layout_customer_item))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: CustomerListViewHolder, position: Int) = holder.bind(data[position], onItemClickListener, onDeleteClicked, isAdmin)

    fun updateData(it: List<CustomerDataModel>) {
        data.clear()
        data.addAll(it)
        notifyDataSetChanged()
    }

    class CustomerListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(customerServiceDataModel: CustomerDataModel, onItemClickListener: (CustomerDataModel) -> Unit, onDeleteClickListener: (CustomerDataModel) -> Unit, isAdmin: Boolean) = with(itemView) {
            textViewCustomerName.text = customerServiceDataModel.customerName
            textViewCustomerEmail.text = customerServiceDataModel.customerEmail
            textViewCustomerPhone.text = customerServiceDataModel.customerPhone
            textViewCustomerService.text = "${customerServiceDataModel.services.size}"
            imageViewDetail.setVisible(true)
            if (isAdmin) {
                imageViewDelete.setVisible(true)
                imageViewDelete.setOnClickListener {
                    onDeleteClickListener(customerServiceDataModel)
                }
            } else {
                imageViewDelete.setVisible(false)
            }
            imageViewNeedToRemind.setVisible(customerServiceDataModel.isNeedToBeReminded)

            setOnClickListener { onItemClickListener(customerServiceDataModel) }
        }
    }
}