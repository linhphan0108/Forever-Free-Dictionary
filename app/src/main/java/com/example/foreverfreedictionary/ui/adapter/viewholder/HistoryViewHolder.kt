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
        with(item){
            itemView.txtWord.text = word
            itemView.txtIpa.text = if (ipaBr != null && ipaAme != null){
                itemView.context.getString(R.string.ipa_format, ipaBr, ipaAme)
            } else if(ipaBr != null){
                itemView.context.getString(R.string.ipa_br_format, ipaBr)
            }else ""
            itemView.txtTimeElapsed.text = lastAccess.howLongTimeLapsedTilNow()
            itemView.iBtnFavorite.setBackgroundColor(if (item.isFavorite) {
                R.color.colorAccent
            }else{
                android.R.color.transparent
            })

            itemView.setOnClickListener { listener.onItemClicked(this) }
            itemView.iBtnFavorite.setOnClickListener { listener.onFavoriteButtonClicked(this) }
        }
    }

    interface OnItemListeners{
        fun onItemClicked(item: HistoryEntity)
        fun onFavoriteButtonClicked(item: HistoryEntity)
    }
}