package com.example.foreverfreedictionary.ui.model

class FavoriteEntity (val query: String,
                        val word: String,
                        val soundBr: String?,
                        val soundAme: String?,
                        val ipaBr: String?,
                        /**american accent*/
                        val ipaAme: String?,
                        val isFavorite: Boolean) : Entity()