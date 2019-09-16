package com.example.foreverfreedictionary.ui.adapter.delegate

import android.view.ViewGroup
import com.example.foreverfreedictionary.ui.adapter.viewholder.AutoCompletionViewHolder
import com.example.foreverfreedictionary.ui.model.AutoCompletionEntity
import com.example.linh.vietkitchen.ui.model.Entity
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class AutoCompletionDelegate(val listener: AutoCompletionViewHolder.OnItemListeners):
    AbsListItemAdapterDelegate<AutoCompletionEntity, Entity, AutoCompletionViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup): AutoCompletionViewHolder {
        return AutoCompletionViewHolder(parent, listener)
    }

    override fun isForViewType(item: Entity, items: MutableList<Entity>, position: Int): Boolean {
        return item is AutoCompletionEntity
    }

    override fun onBindViewHolder(item: AutoCompletionEntity, viewHolder: AutoCompletionViewHolder, payloads: MutableList<Any>) {
        viewHolder.bindView(item)
    }
}