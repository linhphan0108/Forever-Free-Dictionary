package com.example.foreverfreedictionary.ui.model

import java.sql.Date

class HistoryEntity (val word: String,
//                     val content: String,
//                     val soundBr: String,
//                     val soundAme: String,
//                     val ipaBr: String,
//                     /**american accent*/
//                     val ipaAme: String,
//                     val url: String,
                     val lastAccess: Date) : Entity()