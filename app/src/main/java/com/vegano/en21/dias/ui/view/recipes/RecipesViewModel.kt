package com.vegano.en21.dias.ui.view.recipes

import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.vegano.en21.dias.ui.base.BaseViewModel
import com.vegano.en21.dias.ui.common.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RecipesViewModel : BaseViewModel() {

    sealed class Event {
        data class ShowRecipes(val Ingredients: String, val steps: String, val title: String, val image: String): Event()
        data class ShowBackground(val color: Int): Event()
        data class ShowLoad(val isVisible: Boolean): Event()
    }

    private var db = Firebase.firestore
    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    //region ViewModel Input
    fun initFlow(day: Int, typeFood: String) {
        getRecipes(day, typeFood)
        changerBackground(typeFood)
    }

    private fun getRecipes(day: Int, typeFood: String) {
        var ingredients = ""
        var steps = ""
        var title = ""
        var image = IMAGE_DEFAULT
        viewModelScope.launch {
            doAction(Event.ShowLoad(true))
            (Dispatchers.IO)
            val breakfast = db.collection("$day" + DAY).document(typeFood)
            breakfast.get()
                .addOnSuccessListener { document ->
                    document.let {
                        if(document.data?.get(INGREDIENTS) != null) {
                            ingredients = document.data?.get(INGREDIENTS) as String
                        }
                        if(document.data?.get(STEPS) != null) {
                            steps = document.data?.get(STEPS) as String
                        }
                        if(document.data?.get(TITLE) != null) {
                            title = document.data?.get(TITLE) as String
                        }
                        if (document.data?.get(IMAGE) != null && document.data?.get(IMAGE) != "") {
                            image = document.data?.get(IMAGE) as String
                        }
                        doAction(Event.ShowRecipes(Utils.replaceString(ingredients), Utils.replaceString(steps), Utils.replaceString(title), image))
                    }
                }
            doAction(Event.ShowLoad(false))

        }

    }

    private fun changerBackground(typeFood: String) {
        doAction(Event.ShowLoad(true))
        doAction(Event.ShowBackground(Utils.changeColor(typeFood)))
        doAction(Event.ShowLoad(false))
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