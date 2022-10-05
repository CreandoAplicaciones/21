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
    }
}



