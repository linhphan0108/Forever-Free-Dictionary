package com.example.foreverfreedictionary.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.foreverfreedictionary.R
import com.example.foreverfreedictionary.extensions.howLongTimeLapsedTilNow
import com.example.foreverfreedictionary.ui.model.HistoryEntity
import kotlinx.android.synthetic.main.item_view_history.view.*
import kotlinx.android.synthetic.main.item_view_history.view.iBtnFavorite
import kotlinx.android.synthetic.main.item_view_history.view.txtIpa
import kotlinx.android.synthetic.main.item_view_history.view.txtWord

class HistoryViewHolder(parent: ViewGroup, private val listener: OnItemListeners) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_view_history, parent, false)) {

    init {
        itemView.iBtnFavorite.supportImageTintList = ContextCompat.getColorStateList(itemView.context, R.color.selector_btn_favorite_tint_colors)
    }

    fun bindView(item: HistoryEntity){
        with(item){
            itemView.txtWord.text = word
            itemView.txtIpa.text = if (ipaBr != null && ipaAme != null){
                itemView.context.getString(R.string.ipa_format, ipaBr, ipaAme)
            } else if(ipaBr != null){
                itemView.context.getString(R.string.ipa_br_format, ipaBr)
            }else ""
            itemView.txtTimeElapsed.text = lastAccess.howLongTimeLapsedTilNow()
            itemView.iBtnFavorite.isSelected = item.isFavorite
            if (remindTime == null){
                itemView.iBtnReminder.setImageResource(R.drawable.round_alarm_add_black_18)
            }else{
                if (isReminded){
                    itemView.iBtnReminder.setImageResource(R.drawable.round_alarm_on_black_18)
                }else{
                    itemView.iBtnReminder.setImageResource(R.drawable.round_alarm_black_18)
                }
            }

            itemView.setOnClickListener { listener.onItemClicked(this) }
            itemView.iBtnFavorite.setOnClickListener { listener.onFavoriteButtonClicked(this) }
            itemView.iBtnReminder.setOnClickListener {
                if (remindTime == null || isReminded) {
                    listener.onAlarmButtonClicked(this)
                }
            }
        }
    }

    interface OnItemListeners{
        fun onItemClicked(item: HistoryEntity)
        fun onFavoriteButtonClicked(item: HistoryEntity)
        fun onAlarmButtonClicked(item: HistoryEntity)
    }
}