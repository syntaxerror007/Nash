package com.android.nash.home

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.android.nash.R


class HomeMenuAdapter(val items: List<String>, val context: Context, val onClickListener: (String) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var HEADER_TYPE = 0;
    private var ITEM_TYPE = 1;
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == HEADER_TYPE)
            return HomeHeaderViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_menu_item, parent, false))
        else
            return HomeMenuViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_menu_item, parent, false))
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0)
            return HEADER_TYPE
        else
            return ITEM_TYPE
    }

    override fun getItemCount(): Int {
        return items.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HomeHeaderViewHolder) {
            holder.bind()
        } else if (holder is HomeMenuViewHolder){
            holder.bind(items[position - 1], onClickListener)
        }
    }

    class HomeMenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewContent = itemView.findViewById<TextView>(R.id.textViewContent)
        fun bind(content: String, onClickListener: (String) -> Unit) {
            textViewContent.text = content
            itemView.setOnClickListener { onClickListener(content) }
        }
    }

    class HomeHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewHeader = itemView.findViewById<ImageView>(R.id.imageViewHeader)
        fun bind() {
        }
    }

}