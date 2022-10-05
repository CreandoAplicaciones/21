package com.vegano.en21.dias.ui.view.fullday

import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.vegano.en21.dias.save.SaveSheared.Companion.prefs
import com.vegano.en21.dias.ui.base.BaseViewModel
import com.vegano.en21.dias.ui.common.*
import com.vegano.en21.dias.ui.view.recipes.RecipesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class FullDayViewModel : BaseViewModel() {

    sealed class Event {
        object SetUp : Event()
        data class ShowNumberDay(val numberDay: String,) : Event()
        data class ShowBreakfast(val titleBreakfast: String, val imageBreakfast: String) : Event()
        data class ShowLunch (val titleLunch: String, val imageLunch: String) : Event()
        data class ShowDinner(val titleDinner: String, val imageDinner: String) : Event()
        data class GoToRecipes(val day: Int, val typeFood: String) : Event()
        data class ShowLoad(val isVisible: Boolean): Event()

    }

    private var maxDay: Long = 0
    private var db = Firebase.firestore
    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    //region ViewModel Input
    fun initFlow() {
        doAction(Event.SetUp)
        doAction(Event.ShowNumberDay(prefs.getDay().toString()))
        getBreakFast(prefs.getDay())
        maxDay()
    }

    private fun maxDay() {
        viewModelScope.launch {
            val maximum = db.collection("maxDay").document(DAY)
            maximum.get()
                .addOnSuccessListener { document ->
                    document.let {
                        maxDay = document.data?.get("max") as Long
                    }
                }
        }

    }

    private fun getBreakFast(day: Int) {
        viewModelScope.launch {
            doAction(Event.ShowLoad(true))
            val breakfast = db.collection("$day" + DAY).document(BREAKFAST)
            breakfast.get()
                .addOnSuccessListener { document ->
                    document.let {
                        val title = document.data?.get(TITLE) as String
                        val image = document.data?.get(IMAGE) as String
                        doAction(Event.ShowBreakfast(title, image))
                        getLunch(day)
                    }
                }
        }

    }

    private fun getLunch(day: Int) {
        viewModelScope.launch {
            val breakfast = db.collection("$day" + DAY).document(LUNCH)
            breakfast.get()
                .addOnSuccessListener { document ->
                    document.let {
                        val title = document.data?.get(TITLE) as String
                        val image = document.data?.get(IMAGE) as String
                        doAction(Event.ShowLunch(title, image))
                        getDinner(day)
                    }
                }
        }

    }

    private fun getDinner(day: Int) {
        viewModelScope.launch {
            doAction(Event.ShowLoad(true))
            (Dispatchers.IO)
            val breakfast = db.collection("$day" + DAY).document(LUNCH)
            breakfast.get()
                .addOnSuccessListener { document ->
                    document.let {
                        val title = document.data?.get(TITLE) as String
                        val image = document.data?.get(IMAGE) as String
                        doAction(Event.ShowDinner(title, image))

                    }
                }
            doAction(Event.ShowLoad(false))
        }

    }

    fun nextDay() {
        if (prefs.getDay() < maxDay) {
            prefs.saveDay(prefs.getDay() + 1)
        } else {
            prefs.saveDay(1)
        }
        getBreakFast(prefs.getDay())
        getLunch(prefs.getDay())
        getDinner(prefs.getDay())
        doAction(Event.ShowNumberDay(prefs.getDay().toString()))
    }


    fun previousDay() {
        if (prefs.getDay() > 1) {
            prefs.saveDay(prefs.getDay() - 1)
        } else {
            prefs.saveDay(maxDay.toInt())
        }
        getBreakFast(prefs.getDay())
        getLunch(prefs.getDay())
        getDinner(prefs.getDay())
        doAction(Event.ShowNumberDay(prefs.getDay().toString()))
    }

    fun didClickBreakfast() {
        doAction(Event.GoToRecipes(prefs.getDay(), BREAKFAST))
    }
    fun didClickLunch() {
        doAction(Event.GoToRecipes(prefs.getDay(), LUNCH))
    }
    fun didClickDinner() {
        doAction(Event.GoToRecipes(prefs.getDay(), DINNER))
    }

    //endregion

    //region ViewModel Output
    private fun doAction(event: Event) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }
    //endregion
}