package com.example.foreverfreedictionary.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.foreverfreedictionary.R
import com.example.foreverfreedictionary.ui.model.FavoriteEntity
import kotlinx.android.synthetic.main.item_view_favorite.view.*

class FavoriteViewHolder(parent: ViewGroup, private val listener: OnItemListeners) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_view_history, parent, false)) {

    init {
        itemView.iBtnFavorite.supportImageTintList = ContextCompat.getColorStateList(itemView.context, R.color.selector_btn_favorite_tint_colors)
    }

    fun bindView(item: FavoriteEntity){
        with(item){
            itemView.txtWord.text = word
            itemView.txtIpa.text = if (ipaBr != null && ipaAme != null){
                itemView.context.getString(R.string.ipa_format, ipaBr, ipaAme)
            } else if(ipaBr != null){
                itemView.context.getString(R.string.ipa_br_format, ipaBr)
            }else ""
            itemView.iBtnFavorite.supportImageTintList = ContextCompat.getColorStateList(itemView.context, R.color.selector_btn_favorite_tint_colors)
            itemView.iBtnFavorite.isSelected = item.isFavorite

            itemView.setOnClickListener { listener.onItemClicked(this) }
            itemView.iBtnFavorite.setOnClickListener { listener.onFavoriteButtonClicked(this) }
        }
    }

    interface OnItemListeners{
        fun onItemClicked(item: FavoriteEntity)
        fun onFavoriteButtonClicked(item: FavoriteEntity)
    }
}