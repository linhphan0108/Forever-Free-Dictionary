package com.example.foreverfreedictionary.ui.adapter.base

import com.example.foreverfreedictionary.ui.adapter.diffUtil.DiffUtilEntityItemCallbackDelegateFallback
import com.example.foreverfreedictionary.ui.model.Entity
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

abstract class AsyncListEntityDifferDelegationAdapter(
        private val diffUtilCallback: DiffUtilEntityItemCallback = DiffUtilEntityItemCallback()
)
    : AsyncListDifferDelegationAdapter<Entity>(diffUtilCallback){
    init {
        addDiffUtilDelegate(DiffUtilEntityItemCallbackDelegateFallback())
    }

    protected fun addDiffUtilDelegate(diffUtil: DiffUtilEntityItemCallback.DiffUtilEntityItemCallbackDelegate){
        diffUtilCallback.diffUtilDelegateManager.add(diffUtil)
    }
}