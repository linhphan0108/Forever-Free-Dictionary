package com.example.foreverfreedictionary.ui.adapter

import com.example.foreverfreedictionary.ui.adapter.base.AsyncListEntityDifferDelegationAdapter
import com.example.foreverfreedictionary.ui.adapter.delegate.ReminderDelegate
import com.example.foreverfreedictionary.ui.adapter.viewholder.ReminderViewHolder
import com.example.foreverfreedictionary.ui.model.Entity

class ReminderApdater(items: List<Entity>, listener: ReminderViewHolder.OnItemListeners)
    : AsyncListEntityDifferDelegationAdapter()  {
    init {
        delegatesManager.addDelegate(ReminderDelegate(listener))
        setItems(items)
    }
}