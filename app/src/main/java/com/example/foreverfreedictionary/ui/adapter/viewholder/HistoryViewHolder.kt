package com.example.foreverfreedictionary.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foreverfreedictionary.R
import com.example.foreverfreedictionary.extensions.howLongTimeLapsedTilNow
import com.example.foreverfreedictionary.ui.model.HistoryEntity
import kotlinx.android.synthetic.main.item_view_history.view.*
import java.sql.Timestamp

class HistoryViewHolder(parent: ViewGroup, private val listener: OnItemListeners) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_view_history, parent, false)) {

    fun bindView(item: HistoryEntity){
        itemView.txtWord.text = item.word
//        itemView.txtIpa.text = item.ipaAme
        itemView.txtTimeElapsed.text = item.lastAccess.howLongTimeLapsedTilNow()
        itemView.setOnClickListener { listener.onItemClicked(item) }
        itemView.iBtnFavorite.setOnClickListener { listener.onFavoriteButtonClicked(item) }
    }

    interface OnItemListeners{
        fun onItemClicked(item: HistoryEntity)
        fun onFavoriteButtonClicked(item: HistoryEntity)
    }
}