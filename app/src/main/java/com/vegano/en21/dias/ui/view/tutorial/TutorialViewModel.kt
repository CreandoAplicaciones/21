package com.vegano.en21.dias.ui.view.tutorial

import android.view.View
import androidx.lifecycle.*
import com.vegano.en21.dias.R
import com.vegano.en21.dias.ui.base.BaseViewModel


class TutorialViewModel: BaseViewModel() {

    private val _index = MutableLiveData<Int>()
    val text: LiveData<Int> = Transformations.map(_index) {
        when(it){
            1-> R.string.totorial_this_app
            2-> R.string.totorial_your_body
            3-> R.string.tutorial_are_you_ready
            else -> it
        }
    }


    val image: LiveData<Int> = Transformations.map(_index) {
        when(it){
            1-> R.drawable.this_app1
            2-> R.drawable.you_body2
            3-> R.drawable.are_you_ready3
            else -> R.drawable.this_app1
        }
    }

    val btn: LiveData<Int> = Transformations.map(_index) {
        when(it){
            1->  View.INVISIBLE
            2-> View.INVISIBLE
            3-> View.VISIBLE
            else -> View.INVISIBLE
        }
    }

    fun setIndex(index: Int) {
        _index.value = index
    }

    fun didClickOnClickBegin() {
    }
}