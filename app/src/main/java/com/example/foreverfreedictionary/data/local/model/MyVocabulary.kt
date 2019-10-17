package com.example.foreverfreedictionary.data.local.model

data class MyVocabulary (
    val groupId: Long,
    val groupName: String,
    val query: String,
    val word: String,
    val soundBr: String?,
    val soundAme: String?,
    val ipaBr: String?,
    val ipaAme: String?)