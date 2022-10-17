package com.vegano.en21.dias.ui.view.fullday

import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.vegano.en21.dias.save.SaveSheared
import com.vegano.en21.dias.save.SaveSheared.Companion.prefs
import com.vegano.en21.dias.ui.base.BaseViewModel
import com.vegano.en21.dias.ui.common.*
import com.vegano.en21.dias.ui.view.main.MainViewModel
import com.vegano.en21.dias.ui.view.recipes.RecipesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class FullDayViewModel : BaseViewModel() {

    sealed class Event {
        object SetUp : Event()
        object InitialInterstitial : Event()
        data class ShowNumberDay(val numberDay: String,) : Event()
        data class ShowBreakfast(val titleBreakfast: String, val imageBreakfast: String) : Event()
        data class ShowLunch (val titleLunch: String, val imageLunch: String) : Event()
        data class ShowDinner(val titleDinner: String, val imageDinner: String) : Event()
        data class GoToRecipes(val day: Int, val typeFood: String) : Event()
        data class ShowLoad(val isVisible: Boolean): Event()
        data class ShowNextButton(val day: Int): Event()
        data class ShowBackButton(val day: Int): Event()
        data class ShowInterstitial(val isVisible: Boolean): Event()
    }

    private var maxDay: Long = 21
    private var interstitial = false
    private var numberClick: Long = 10
    private var db = Firebase.firestore
    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    //region ViewModel Input
    fun initFlow() {
        doAction(Event.SetUp)
        doAction(Event.InitialInterstitial)
        doAction(Event.ShowNumberDay(prefs.getDay().toString()))
        numberClick = prefs.getClick()
        getBreakFast(prefs.getDay())
        getAdmobInterstitial()
        maxDay()
    }

    private fun maxDay() {
        viewModelScope.launch {
            val maximum = db.collection("maxDay").document(DAY)
            maximum.get()
                .addOnSuccessListener { document ->
                    document?.let {
                        if(document.data?.get("max") != null){
                            maxDay = document.data?.get("max") as Long
                        }
                        doAction(Event.ShowNextButton(Utils.calculateNextDay(prefs.getDay(), maxDay.toInt())))
                        doAction(Event.ShowBackButton(Utils.calculateBackDay(prefs.getDay(), maxDay.toInt())))
                    }
                }
        }

    }

    private fun getBreakFast(day: Int) {
        var title = ""
        var image = IMAGE_DEFAULT
        viewModelScope.launch {
            doAction(Event.ShowLoad(true))
            val breakfast = db.collection("$day" + DAY).document(BREAKFAST)
            breakfast.get()
                .addOnSuccessListener { document ->
                    document?.let {
                        if (document.data?.get(TITLE) != null ) {
                            title = document.data?.get(TITLE) as String
                        }
                        if (document.data?.get(IMAGE) != null && document.data?.get(IMAGE) != "" ) {
                            image = document.data?.get(IMAGE) as String
                        }
                        doAction(Event.ShowBreakfast(Utils.replaceString(title) , image))
                        getLunch(day)
                    }
                }
        }

    }

    private fun getLunch(day: Int) {
        var title = ""
        var image = IMAGE_DEFAULT
        viewModelScope.launch {
            val breakfast = db.collection("$day" + DAY).document(LUNCH)
            breakfast.get()
                .addOnSuccessListener { document ->
                    document?.let {
                        if (document.data?.get(TITLE) != null ) {
                            title = document.data?.get(TITLE) as String
                        }
                        if (document.data?.get(IMAGE) != null && document.data?.get(IMAGE) != "" ) {
                            image = document.data?.get(IMAGE) as String
                        }
                        doAction(Event.ShowLunch(title, image))
                        getDinner(day)
                    }
                }
        }

    }

    private fun getDinner(day: Int) {
        var title = ""
        var image = IMAGE_DEFAULT
        viewModelScope.launch {
            doAction(Event.ShowLoad(true))
            (Dispatchers.IO)
            val breakfast = db.collection("$day" + DAY).document(DINNER)
            breakfast.get()
                .addOnSuccessListener { document ->
                    document?.let {
                        if (document.data?.get(TITLE) != null ) {
                            title = document.data?.get(TITLE) as String
                        }
                        if (document.data?.get(IMAGE) != null && document.data?.get(IMAGE) != "" ) {
                            image = document.data?.get(IMAGE) as String
                        }
                        doAction(Event.ShowDinner(title, image))

                    }
                }
            doAction(Event.ShowLoad(false))
        }

    }


    private fun getAdmobInterstitial() {
        viewModelScope.launch {
            val maximum = db.collection(ADMOB).document(INTERSTICIAL)
            maximum.get()
                .addOnSuccessListener { document ->
                    document?.let {
                        if(document.data?.get(SHOW_INTERSTICIAL) != null){
                            interstitial = document.data?.get(SHOW_INTERSTICIAL) as Boolean
                        }
                    }
                }
        }
    }

    private fun getNumberInterstitial() {
        viewModelScope.launch {
            val maximum = db.collection(ADMOB).document(INTERSTICIAL)
            maximum.get()
                .addOnSuccessListener { document ->
                    document?.let {
                        if(document.data?.get(NUMBER_INTERSTITIAL) != null){
                            numberClick = document.data?.get(NUMBER_INTERSTITIAL) as Long
                            prefs.saveClick(numberClick)
                        }
                    }
                }
        }
    }

    private fun didOnClick() {
        if (numberClick <= 0) {
            doAction(Event.ShowInterstitial(interstitial))
            getNumberInterstitial()
        } else {
            numberClick -= 1
            prefs.saveClick(numberClick-1)
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
        doAction(Event.ShowNextButton(Utils.calculateNextDay(prefs.getDay(), maxDay.toInt())))
        doAction(Event.ShowBackButton(Utils.calculateBackDay(prefs.getDay(), maxDay.toInt())))
        didOnClick()
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
        doAction(Event.ShowNextButton(Utils.calculateNextDay(prefs.getDay(), maxDay.toInt())))
        doAction(Event.ShowBackButton(Utils.calculateBackDay(prefs.getDay(), maxDay.toInt())))
        didOnClick()
    }

    fun didClickBreakfast() {
        doAction(Event.GoToRecipes(prefs.getDay(), BREAKFAST))
        didOnClick()
    }
    fun didClickLunch() {
        doAction(Event.GoToRecipes(prefs.getDay(), LUNCH))
        didOnClick()
    }
    fun didClickDinner() {
        doAction(Event.GoToRecipes(prefs.getDay(), DINNER))
        didOnClick()
    }
    fun showedInterstitial() {
        doAction(Event.InitialInterstitial)
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