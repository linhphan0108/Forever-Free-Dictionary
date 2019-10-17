package com.example.foreverfreedictionary.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foreverfreedictionary.R
import com.example.foreverfreedictionary.ui.model.MyVocabularyGroupEntity
import kotlinx.android.synthetic.main.item_view_my_vocabulary_group_picker.view.*

class MyVocabularyGroupPickerViewHolder (parent: ViewGroup, private val listener: OnItemListeners) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_view_my_vocabulary_group_picker, parent, false)) {

    fun bindView(item: MyVocabularyGroupEntity) {
        with(item){
            itemView.txtGroupName.text = name
            itemView.checkbox.isChecked = item.isSelected

            itemView.setOnClickListener { listener.onItemClick(item) }
            itemView.checkbox.setOnCheckedChangeListener { _, isChecked ->
                listener.onCheckedChanged(item, isChecked)
            }
        }

    }

    interface OnItemListeners{
        fun onItemClick(item: MyVocabularyGroupEntity)
        fun onCheckedChanged(item: MyVocabularyGroupEntity, isChecked: Boolean)
    }
}