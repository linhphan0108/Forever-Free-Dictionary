package com.example.foreverfreedictionary.data.local.model

class FavoriteDictionary(val query: String,
                      val word: String,
                      val soundBr: String?,
                      val soundAme: String?,
                      val ipaBr: String?,
                      val ipaAme: String?,
                      val isFavorite: Boolean)