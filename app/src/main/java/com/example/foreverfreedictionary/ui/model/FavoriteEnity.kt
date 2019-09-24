package com.example.foreverfreedictionary.ui.model

import com.google.gson.annotations.SerializedName
import java.sql.Date

class FavoriteEntity (val query: String,
                        val word: String,
                        val soundBr: String?,
                        val soundAme: String?,
                        val ipaBr: String?,
                        /**american accent*/
                        val ipaAme: String?,
                        val isFavorite: Boolean,
                        val lastAccess: Date) : Entity()