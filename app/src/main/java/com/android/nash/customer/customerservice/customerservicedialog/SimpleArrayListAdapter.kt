package com.android.nash.customer.customerservice.customerservicedialog

import android.text.TextUtils
import com.android.nash.R
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.*
import gr.escsoft.michaelprimez.searchablespinner.interfaces.ISpinnerSelectedView


class SimpleArrayListAdapter<T>(private val mContext: Context, private var mData: List<T>) : ArrayAdapter<T>(mContext, R.layout.view_list_item), Filterable, ISpinnerSelectedView {
    private val mBackupStrings: List<T> = mData
    private val mStringFilter = StringFilter()

    override fun getCount(): Int {
        return mData.size + 1
    }

    override fun getItem(position: Int): T? {
        return if (position > 0)
            mData[position - 1]
        else
            null
    }

    override fun getItemId(position: Int): Long {
        return if (position > 0)
            mData[position]!!.hashCode().toLong()
        else
            -1
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return if (position == 0) {
            noSelectionView
        } else {
            val view = View.inflate(mContext, R.layout.view_list_item, null)
            val displayName = view.findViewById(R.id.TxtVw_DisplayName) as TextView
            displayName.text = mData[position - 1].toString()
            view
        }
    }

    override fun getSelectedView(position: Int): View {
        val view: View?
        if (position == 0) {
            view = noSelectionView
        } else {
            view = View.inflate(mContext, R.layout.view_list_item, null)
            val displayName = view!!.findViewById(R.id.TxtVw_DisplayName) as TextView
            displayName.text = mData[position - 1].toString()
        }
        return view
    }

    override fun getNoSelectionView(): View {
        return View.inflate(mContext, R.layout.view_list_no_selection_item, null)
    }

    override fun getFilter(): Filter {
        return mStringFilter
    }

    inner class StringFilter : Filter() {

        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filterResults = FilterResults()
            if (TextUtils.isEmpty(constraint)) {
                filterResults.count = mBackupStrings.size
                filterResults.values = mBackupStrings
                return filterResults
            }
            val filterStrings = mutableListOf<T>()
            for (text in mBackupStrings) {
                if (text.toString().toLowerCase().contains(constraint)) {
                    filterStrings.add(text)
                }
            }
            filterResults.count = filterStrings.size
            filterResults.values = filterStrings
            return filterResults
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            mData = results.values as List<T>
            notifyDataSetChanged()
        }
    }
}