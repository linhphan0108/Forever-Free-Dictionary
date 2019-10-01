package com.example.foreverfreedictionary.ui.model

import java.sql.Date

enum class ReminderStates{
    NOT_YET, ON_GOING, REMINDED;

    companion object {
        fun from(reminded: Boolean, remindTime: Date?): ReminderStates{
            return if (reminded){
                REMINDED
            }else if(!reminded && remindTime != null){
                ON_GOING
            }else{
                NOT_YET
            }
        }
    }
}