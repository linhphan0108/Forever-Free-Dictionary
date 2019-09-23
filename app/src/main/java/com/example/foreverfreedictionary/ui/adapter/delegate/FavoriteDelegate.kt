package com.example.foreverfreedictionary.ui.adapter.delegate

import android.view.ViewGroup
import com.example.foreverfreedictionary.ui.adapter.viewholder.FavoriteViewHolder
import com.example.foreverfreedictionary.ui.model.Entity
import com.example.foreverfreedictionary.ui.model.FavoriteEntity
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class FavoriteDelegate(private val listener: FavoriteViewHolder.OnItemListeners)
    : AbsListItemAdapterDelegate<FavoriteEntity, Entity, FavoriteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup): FavoriteViewHolder {
        return FavoriteViewHolder(parent, listener)
    }

    override fun isForViewType(item: Entity, items: MutableList<Entity>, position: Int): Boolean {
        return item is FavoriteEntity
    }

    override fun onBindViewHolder(
        item: FavoriteEntity,
        holder: FavoriteViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.bindView(item)
    }
}