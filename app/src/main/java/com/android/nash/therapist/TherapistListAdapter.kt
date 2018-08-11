package com.android.nash.therapist

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.android.nash.R
import com.android.nash.data.TherapistDataModel
import com.android.nash.util.convertToString
import com.android.nash.util.inflate
import kotlinx.android.synthetic.main.layout_therapist_item.view.*

class TherapistListAdapter(private val items: List<TherapistDataModel>, private val listener: (TherapistDataModel) -> Unit) : RecyclerView.Adapter<TherapistListAdapter.TherapistListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TherapistListViewHolder = TherapistListViewHolder(parent.inflate(R.layout.layout_therapist_item))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TherapistListViewHolder, position: Int) = holder.bind(items[position], listener)

    class TherapistListViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bind(therapistDataModel: TherapistDataModel, listener: (TherapistDataModel) -> Unit) = with(itemView) {
            textViewTherapistName.text = therapistDataModel.therapistName
            textViewPhoneNumber.text = "Phone Number: ${therapistDataModel.phoneNumber}"
            textViewWorkSince.text = "Work Since: ${therapistDataModel.workSince.convertToString()}"
            textViewJobs.text = "Jobs: ${therapistDataModel.job}"
            itemView.setOnClickListener { listener(therapistDataModel) }
        }

    }
}