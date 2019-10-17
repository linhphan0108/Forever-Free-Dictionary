package com.example.foreverfreedictionary.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foreverfreedictionary.R
import com.example.foreverfreedictionary.ui.model.MyVocabularyEntity
import kotlinx.android.synthetic.main.item_view_my_vocabulary.view.*

class MyVocabularyViewHolder (parent: ViewGroup, private val listener: OnItemListeners?) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_view_my_vocabulary, parent, false)) {

    fun bindView(item: MyVocabularyEntity) {
        with(item){
            itemView.txtWord.text = word

            itemView.setOnClickListener { listener?.onItemClick(item) }
            itemView.iBtnDelete.setOnClickListener { listener?.delete(query, groupName)}
        }

    }

    interface OnItemListeners{
        fun onItemClick(item: MyVocabularyEntity)
        fun delete(query: String, groupName: String)
    }
}