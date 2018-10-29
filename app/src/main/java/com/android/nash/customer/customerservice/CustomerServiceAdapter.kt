package com.android.nash.customer.customerservice

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.android.nash.R
import com.android.nash.data.CustomerServiceDataModel
import com.android.nash.util.convertToPrice
import com.android.nash.util.convertToString
import com.android.nash.util.inflate
import kotlinx.android.synthetic.main.layout_service_customer_item.view.*

class CustomerServiceAdapter(val list : List<CustomerServiceDataModel>) : RecyclerView.Adapter<CustomerServiceAdapter.CustomerServiceViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerServiceViewHolder = CustomerServiceViewHolder(parent.inflate(R.layout.layout_service_customer_item))

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: CustomerServiceViewHolder, position: Int) = holder.bind(list[position])

    class CustomerServiceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(customerServiceDataModel: CustomerServiceDataModel) = with(itemView) {
            textViewCustomerServiceDate.text = customerServiceDataModel.treatmentDate.convertToString()
            textViewCustomerServiceName.text = customerServiceDataModel.service.serviceName
            textViewCustomerServicePrice.text = customerServiceDataModel.price.convertToPrice()
            textViewCustomerServiceTherapist.text = customerServiceDataModel.therapist.therapistName
            textViewCustomerServiceLocation.text = customerServiceDataModel.locationName
        }
    }
}

