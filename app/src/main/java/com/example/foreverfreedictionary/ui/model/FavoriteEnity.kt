package com.example.foreverfreedictionary.ui.model

import java.sql.Date

class FavoriteEntity (val query: String,
                        val word: String,
                        val ipaBr: String?,
                        /**american accent*/
                        val ipaAme: String?,
                        val isFavorite: Boolean,
                        val lastAccess: Date) : Entity()