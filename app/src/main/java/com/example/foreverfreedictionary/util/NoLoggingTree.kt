package com.example.foreverfreedictionary.util

import timber.log.Timber

class NoLoggingTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
    }
}