package com.example.foreverfreedictionary.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foreverfreedictionary.R
import com.example.foreverfreedictionary.ui.model.MyVocabularyGroupEntity
import kotlinx.android.synthetic.main.item_view_my_vocabulary_group.view.*

class MyVocabularyGroupViewHolder (parent: ViewGroup, private val listener: OnItemListeners?) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_view_my_vocabulary_group, parent, false)) {

    fun bindView(item: MyVocabularyGroupEntity) {
        with(item){
            itemView.txtWord.text = name

            itemView.setOnClickListener { listener?.onItemClick(item) }
            itemView.iBtnDelete.setOnClickListener { listener?.delete(name)}
        }

    }

    interface OnItemListeners{
        fun onItemClick(item: MyVocabularyGroupEntity)
        fun delete(groupName: String)
    }
}