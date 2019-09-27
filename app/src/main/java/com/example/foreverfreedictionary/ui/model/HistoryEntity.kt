package com.example.foreverfreedictionary.ui.model

import java.sql.Date

class HistoryEntity (val query: String,
                    val word: String,
                     val soundBr: String?,
                     val soundAme: String?,
                     val ipaBr: String?,
                     /**american accent*/
                     val ipaAme: String?,
//                     val url: String,
                     val isFavorite: Boolean,
                     val isReminded: Boolean,
                     val remindTime: Date?,
                     val lastAccess: Date) : Entity()