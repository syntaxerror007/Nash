package com.nash.android.therapist

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.nash.android.R
import com.nash.android.data.TherapistDataModel
import com.nash.android.util.convertToString
import com.nash.android.util.inflate
import kotlinx.android.synthetic.main.layout_therapist_item.view.*

class TherapistListAdapter(private val items: List<TherapistDataModel>, private val therapistListCallback: TherapistListCallback) : RecyclerView.Adapter<TherapistListAdapter.TherapistListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TherapistListViewHolder = TherapistListViewHolder(parent.inflate(R.layout.layout_therapist_item))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TherapistListViewHolder, position: Int) = holder.bind(items[position], therapistListCallback, position)

    class TherapistListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(therapistDataModel: TherapistDataModel, therapistListCallback: TherapistListCallback, position: Int) = with(itemView) {
            textViewTherapistName.text = therapistDataModel.therapistName
            textViewPhoneNumber.text = "Phone Number: ${therapistDataModel.phoneNumber}"
            textViewWorkSince.text = "Work Since: ${therapistDataModel.workSince.convertToString()}"
            textViewJobs.text = "Jobs: ${therapistDataModel.job}"
            buttonEdit.setOnClickListener { therapistListCallback.onTherapistEdit(therapistDataModel, position) }
            buttonDelete.setOnClickListener { therapistListCallback.onTherapistDelete(therapistDataModel, position) }
        }

    }
}