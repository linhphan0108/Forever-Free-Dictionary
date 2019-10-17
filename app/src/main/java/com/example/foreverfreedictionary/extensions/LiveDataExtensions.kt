package com.example.foreverfreedictionary.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <A, B, R> zipLiveData(a: LiveData<A>, b: LiveData<B>, onUpdate: (a: A, b: B) -> R): LiveData<R> {
    return MediatorLiveData<R>().apply {
        var lastA: A? = null
        var lastB: B? = null

        fun update() {
            val localLastA = lastA
            val localLastB = lastB
            if (localLastA != null && localLastB != null)
                this.value = onUpdate.invoke(localLastA, localLastB)
        }

        addSource(a) {
            lastA = it
            update()
        }
        addSource(b) {
            lastB = it
            update()
        }
    }
}

fun <A, B, R>MediatorLiveData<R>.zipLiveData(a: LiveData<A>, b: LiveData<B>, onUpdate: (a: A, b: B) -> R){
    var lastA: A? = null
    var lastB: B? = null

    fun update() {
        val localLastA = lastA
        val localLastB = lastB
        if (localLastA != null && localLastB != null)
            this.value = onUpdate.invoke(localLastA, localLastB)
    }

    addSource(a) {
        lastA = it
        update()
    }
    addSource(b) {
        lastB = it
        update()
    }
}