package com.vegano.en21.dias.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vegano.en21.dias.ui.base.BaseEvent.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel: ViewModel() {

    private val baseEventChannel = Channel<BaseEvent>(Channel.BUFFERED)
    val baseEventsFlow = baseEventChannel.receiveAsFlow()

    /**
     * handleError send a BaseEvent from BaseViewModel to BaseActivity or BaseFragment
     */

    fun showLoading(visibility: Boolean) = doEvent(ShowLoading(visibility))

    fun showMessage(resId: Int) = doEvent(ShowMessage(resId))

    private fun doEvent(event: BaseEvent) {
        viewModelScope.launch {
            baseEventChannel.send(event)
        }
    }
}