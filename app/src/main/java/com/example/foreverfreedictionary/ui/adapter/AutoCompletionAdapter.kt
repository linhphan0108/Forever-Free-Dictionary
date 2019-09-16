package com.example.foreverfreedictionary.ui.adapter

import com.example.foreverfreedictionary.ui.adapter.base.AsyncListEntityDifferDelegationAdapter
import com.example.foreverfreedictionary.ui.adapter.delegate.AutoCompletionDelegate
import com.example.foreverfreedictionary.ui.adapter.diffUtil.AutoCompletionDiffUtilCallback
import com.example.foreverfreedictionary.ui.adapter.viewholder.AutoCompletionViewHolder
import com.example.foreverfreedictionary.ui.model.Entity

class AutoCompletionAdapter(items: List<Entity> = mutableListOf(),
                            listener: AutoCompletionViewHolder.OnItemListeners)
    : AsyncListEntityDifferDelegationAdapter(){
    init {
        delegatesManager.addDelegate(AutoCompletionDelegate(listener))
        addDiffUtilDelegate(AutoCompletionDiffUtilCallback())
        setItems(items)
    }
}