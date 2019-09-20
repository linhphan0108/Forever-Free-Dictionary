package com.example.foreverfreedictionary.ui.adapter

import com.example.foreverfreedictionary.ui.adapter.base.AsyncListEntityDifferDelegationAdapter
import com.example.foreverfreedictionary.ui.adapter.delegate.HistoryDelegate
import com.example.foreverfreedictionary.ui.adapter.viewholder.HistoryViewHolder
import com.example.foreverfreedictionary.ui.model.Entity

class HistoryAdapter(items: List<Entity>, listener: HistoryViewHolder.OnItemListeners)
    : AsyncListEntityDifferDelegationAdapter() {
    init {
        delegatesManager.addDelegate(HistoryDelegate(listener))
        setItems(items)
    }
}