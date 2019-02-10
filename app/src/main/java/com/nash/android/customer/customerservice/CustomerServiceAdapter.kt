package com.nash.android.customer.customerservice

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.nash.android.R
import com.nash.android.data.CustomerServiceDataModel
import com.nash.android.util.convertToPrice
import com.nash.android.util.convertToString
import com.nash.android.util.inflate
import kotlinx.android.synthetic.main.layout_service_customer_item.view.*

class CustomerServiceAdapter(val list: List<CustomerServiceDataModel>, private val onItemClick: (CustomerServiceDataModel) -> Unit) : RecyclerView.Adapter<CustomerServiceAdapter.CustomerServiceViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerServiceViewHolder = CustomerServiceViewHolder(parent.inflate(R.layout.layout_service_customer_item))

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: CustomerServiceViewHolder, position: Int) = holder.bind(list[position], onItemClick)

    class CustomerServiceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(customerServiceDataModel: CustomerServiceDataModel, onItemClick: (CustomerServiceDataModel) -> Unit) = with(itemView) {
            textViewCustomerServiceDate.text = customerServiceDataModel.treatmentDate.convertToString()
            textViewCustomerServiceName.text = customerServiceDataModel.service?.serviceName
            textViewCustomerServicePrice.text = customerServiceDataModel.price.convertToPrice()
            textViewCustomerServiceTherapist.text = customerServiceDataModel.therapist?.therapistName
            textViewCustomerServiceLocation.text = customerServiceDataModel.locationName
            setOnClickListener { onItemClick.invoke(customerServiceDataModel) }
        }
    }
}

