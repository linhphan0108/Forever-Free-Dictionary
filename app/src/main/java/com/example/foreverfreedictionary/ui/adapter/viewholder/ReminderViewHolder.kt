package com.example.foreverfreedictionary.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.foreverfreedictionary.R
import com.example.foreverfreedictionary.ui.model.ReminderEntity
import kotlinx.android.synthetic.main.item_view_reminder.view.*
import kotlinx.android.synthetic.main.item_view_reminder.view.iBtnDelete
import kotlinx.android.synthetic.main.item_view_reminder.view.txtIpa
import kotlinx.android.synthetic.main.item_view_reminder.view.txtWord

class ReminderViewHolder (parent: ViewGroup, private val listener: OnItemListeners) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_view_reminder, parent, false)) {

    init {
        itemView.iBtnSetReminder.supportImageTintList = ContextCompat.getColorStateList(itemView.context, R.color.selector_btn_reminder_tint_colors)
    }

    fun bindView(item: ReminderEntity){
        with(item){
            itemView.txtWord.text = word
            itemView.txtIpa.text = if (ipaBr != null && ipaAme != null){
                itemView.context.getString(R.string.ipa_format, ipaBr, ipaAme)
            } else if(ipaBr != null){
                itemView.context.getString(R.string.ipa_br_format, ipaBr)
            }else ""
            itemView.iBtnSetReminder.setImageResource(
                if (isReminded) R.drawable.round_alarm_on_black_18
                else R.drawable.round_alarm_black_18)
            itemView.iBtnSetReminder.isActivated = !isReminded

            itemView.setOnClickListener { listener.onItemClicked(this) }
            itemView.iBtnDelete.setOnClickListener { listener.onDeleteButtonClicked(this) }
            itemView.iBtnSetReminder.setOnClickListener {
                listener.onSetReminderButtonClicked(this)
            }
        }
    }

    interface OnItemListeners{
        fun onItemClicked(item: ReminderEntity)
        fun onDeleteButtonClicked(item: ReminderEntity)
        fun onSetReminderButtonClicked(item: ReminderEntity)
    }
}