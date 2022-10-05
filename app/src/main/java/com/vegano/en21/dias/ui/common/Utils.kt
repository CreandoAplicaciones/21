package com.vegano.en21.dias.ui.common

import com.vegano.en21.dias.R

class Utils {

    companion object {
        fun changeColor(typeFood: String): Int {
            return when(typeFood){
                BREAKFAST -> R.color.background_pink
                LUNCH -> R.color.background_orange
                DINNER -> R.color.background_purple
                else -> R.color.background_pink
            }
        }

        fun calculateNextDay(day: Int, maxDay: Int): Int {
            return when(day){
                maxDay -> 1
                else -> day + 1
            }
        }

        fun calculateBackDay(day: Int, maxDay: Int): Int {
            return when(day){
                1 -> maxDay
                else -> day - 1
            }
        }

        fun replaceString(text: String): String = text.replace("..","\n")

    }
}



