package com.example.foreverfreedictionary.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.foreverfreedictionary.R
import com.example.foreverfreedictionary.ui.model.AutoCompletionEntity
import kotlinx.android.synthetic.main.item_view_auto_completion.view.*

class AutoCompletionViewHolder(val parent: ViewGroup, private val listener: OnItemListeners)
    : androidx.recyclerview.widget.RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_view_auto_completion, parent, false)) {

    fun bindView(item: AutoCompletionEntity){
        itemView.txtSuggestion.text = item.value
        itemView.setOnClickListener{
            listener.onItemClick(item)
        }
    }

    interface OnItemListeners{
        fun onItemClick(item: AutoCompletionEntity)
    }
}