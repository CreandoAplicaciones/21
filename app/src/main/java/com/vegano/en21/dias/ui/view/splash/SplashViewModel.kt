package com.vegano.en21.dias.ui.view.splash

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.viewModelScope
import com.vegano.en21.dias.save.SaveSheared
import com.vegano.en21.dias.ui.base.BaseViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


class SplashViewModel (): BaseViewModel() {

    sealed class Event {
        object GoToMain: Event()
        object GoToTutorial: Event()
    }

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    init {
        checkIfIsFirstTime()
    }

    private fun checkIfIsFirstTime() {
        if (SaveSheared.prefs.getFirstDay()) {
            doEvent(Event.GoToMain)
        } else {
            doEvent(Event.GoToTutorial)
        }
    }

    //region output
    private fun doEvent(event: Event) {
        Handler(Looper.getMainLooper()).postDelayed({
            viewModelScope.launch {
                eventChannel.send(event)
            }
        },1500)
    }
    //endregion
}