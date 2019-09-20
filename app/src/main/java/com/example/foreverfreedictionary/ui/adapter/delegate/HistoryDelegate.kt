package com.example.foreverfreedictionary.ui.adapter.delegate

import android.view.ViewGroup
import com.example.foreverfreedictionary.ui.adapter.viewholder.HistoryViewHolder
import com.example.foreverfreedictionary.ui.model.Entity
import com.example.foreverfreedictionary.ui.model.HistoryEntity
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class HistoryDelegate(private val listener: HistoryViewHolder.OnItemListeners)
    : AbsListItemAdapterDelegate<HistoryEntity, Entity, HistoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup): HistoryViewHolder {
        return HistoryViewHolder(parent, listener)
    }

    override fun isForViewType(item: Entity, items: MutableList<Entity>, position: Int): Boolean {
        return item is HistoryEntity
    }

    override fun onBindViewHolder(
        item: HistoryEntity,
        holder: HistoryViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.bindView(item)
    }
}