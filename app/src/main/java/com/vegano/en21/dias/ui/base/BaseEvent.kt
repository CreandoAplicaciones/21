package com.vegano.en21.dias.ui.base

sealed class BaseEvent {
    data class ShowMessage(val message: Int): BaseEvent()
    data class ShowLoading(val visibility: Boolean): BaseEvent()
}