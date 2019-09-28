package com.example.foreverfreedictionary.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.foreverfreedictionary.R
import com.example.foreverfreedictionary.ui.model.FavoriteEntity
import kotlinx.android.synthetic.main.item_view_favorite.view.*
import kotlinx.android.synthetic.main.item_view_favorite.view.iBtnDelete
import kotlinx.android.synthetic.main.item_view_favorite.view.iBtnReminder
import kotlinx.android.synthetic.main.item_view_favorite.view.txtIpa
import kotlinx.android.synthetic.main.item_view_favorite.view.txtWord

class FavoriteViewHolder(parent: ViewGroup, private val listener: OnItemListeners) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_view_favorite, parent, false)) {

    init {
        itemView.iBtnReminder.supportImageTintList = ContextCompat.getColorStateList(itemView.context, R.color.selector_btn_reminder_tint_colors)
    }

    fun bindView(item: FavoriteEntity){
        with(item){
            itemView.txtWord.text = word
            itemView.txtIpa.text = if (ipaBr != null && ipaAme != null){
                itemView.context.getString(R.string.ipa_format, ipaBr, ipaAme)
            } else if(ipaBr != null){
                itemView.context.getString(R.string.ipa_br_format, ipaBr)
            }else ""

            if (remindTime == null){
                itemView.iBtnReminder.setImageResource(R.drawable.round_alarm_add_black_18)
            }else{
                if (isReminded){
                    itemView.iBtnReminder.setImageResource(R.drawable.round_alarm_on_black_18)
                }else{
                    itemView.iBtnReminder.setImageResource(R.drawable.round_alarm_black_18)
                }
            }
            itemView.iBtnReminder.isActivated = remindTime != null && !isReminded

            itemView.setOnClickListener { listener.onItemClicked(this) }
            itemView.iBtnDelete.setOnClickListener { listener.onFavoriteButtonClicked(this) }
            itemView.iBtnReminder.setOnClickListener {
                if (remindTime == null || isReminded) {
                    listener.onSetReminderButtonClicked(this)
                }
            }
        }
    }

    interface OnItemListeners{
        fun onItemClicked(item: FavoriteEntity)
        fun onFavoriteButtonClicked(item: FavoriteEntity)
        fun onSetReminderButtonClicked(item: FavoriteEntity)
    }
}