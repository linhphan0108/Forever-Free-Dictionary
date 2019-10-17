package com.example.foreverfreedictionary.ui.model

class MyVocabularyEntity(
    val groupName: String,
    val query: String,
    val word: String,
    val soundBr: String?,
    val soundAme: String?,
    val ipaBr: String?,
    val ipaAme: String?
) : Entity()