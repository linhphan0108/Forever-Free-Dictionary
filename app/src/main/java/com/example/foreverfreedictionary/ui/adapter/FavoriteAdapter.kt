package com.example.foreverfreedictionary.ui.adapter

import com.example.foreverfreedictionary.ui.adapter.base.AsyncListEntityDifferDelegationAdapter
import com.example.foreverfreedictionary.ui.adapter.delegate.FavoriteDelegate
import com.example.foreverfreedictionary.ui.adapter.viewholder.FavoriteViewHolder
import com.example.foreverfreedictionary.ui.model.Entity

class FavoriteAdapter (items: List<Entity>, listener: FavoriteViewHolder.OnItemListeners)
    : AsyncListEntityDifferDelegationAdapter()  {
    init {
        delegatesManager.addDelegate(FavoriteDelegate(listener))
        setItems(items)
    }
}