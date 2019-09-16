package com.example.foreverfreedictionary.ui.adapter

import com.example.foreverfreedictionary.ui.adapter.delegate.AutoCompletionDelegate
import com.example.foreverfreedictionary.ui.adapter.viewholder.AutoCompletionViewHolder
import com.example.linh.vietkitchen.ui.model.Entity
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter

class AutoCompletionAdapter(items: List<Entity> = mutableListOf(),
                            private val listener: AutoCompletionViewHolder.OnItemListeners)
    : ListDelegationAdapter<List<Entity>>(){
    init {
        delegatesManager.addDelegate(AutoCompletionDelegate(listener))
        setItems(items)
    }

    override fun setItems(items: List<Entity>){
        super.setItems(items)
        notifyDataSetChanged()
    }
}