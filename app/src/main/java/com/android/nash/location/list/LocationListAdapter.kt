package com.android.nash.location.list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.android.nash.R
import com.android.nash.data.LocationDataModel
import com.android.nash.util.inflate
import kotlinx.android.synthetic.main.layout_location_item.view.*

class LocationListAdapter(private val items: List<LocationDataModel>, private val listener: (LocationDataModel) -> Unit, private val deleteListener: (LocationDataModel) -> Unit) : RecyclerView.Adapter<LocationListAdapter.LocationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder = LocationViewHolder(parent.inflate(R.layout.layout_location_item))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) = holder.bind(items[position], listener, deleteListener)

    class LocationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(locationDataModel: LocationDataModel, listener: (LocationDataModel) -> Unit, deleteListener: (LocationDataModel) -> Unit) = with(itemView) {
            itemView.setOnClickListener { listener.invoke(locationDataModel) }
            imageViewRemove.setOnClickListener { deleteListener.invoke(locationDataModel) }
            textViewLocationName.text = locationDataModel.locationName
            textViewAddress.text = "Address: ${locationDataModel.locationAddress}"
            textViewPhoneNumber.text = "Phone Number: ${locationDataModel.phoneNumber}"
            textViewTotalServices.text = "Total Services: ${locationDataModel.totalServices}"
            textViewTotalTherapist.text = "Total Therapist: ${locationDataModel.therapists.size}"
        }
    }
}