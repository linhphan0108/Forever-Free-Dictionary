package com.example.foreverfreedictionary.ui.adapter.delegate

import android.view.ViewGroup
import com.example.foreverfreedictionary.ui.adapter.viewholder.ReminderViewHolder
import com.example.foreverfreedictionary.ui.model.Entity
import com.example.foreverfreedictionary.ui.model.ReminderEntity
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class ReminderDelegate(private val listener: ReminderViewHolder.OnItemListeners)
    : AbsListItemAdapterDelegate<ReminderEntity, Entity, ReminderViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup): ReminderViewHolder {
        return ReminderViewHolder(parent, listener)
    }

    override fun isForViewType(item: Entity, items: MutableList<Entity>, position: Int): Boolean {
        return item is ReminderEntity
    }

    override fun onBindViewHolder(
        item: ReminderEntity,
        holder: ReminderViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.bindView(item)
    }
}