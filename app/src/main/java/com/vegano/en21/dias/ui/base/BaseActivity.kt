package com.vegano.en21.dias.ui.base

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.vegano.en21.dias.ui.common.extension.observe
import com.vegano.en21.dias.R
import kotlinx.coroutines.flow.Flow


abstract class BaseActivity: AppCompatActivity() {

    private lateinit var eventsFlow: Flow<BaseEvent>
    private lateinit var baseProgressBar: BaseProgressBar

    override fun onDestroy() {
        if (::baseProgressBar.isInitialized) baseProgressBar.removeView()
        super.onDestroy()
    }

    fun init(baseViewModel: BaseViewModel) {
        this.eventsFlow = baseViewModel.baseEventsFlow
        this.eventsFlow.observe(this, ::observeEvent)
        this.baseProgressBar = BaseProgressBar(this, R.color.black)
    }

    private fun observeEvent(event: BaseEvent) {
        when(event) {
            is BaseEvent.ShowLoading -> showProgress(event.visibility)
            is BaseEvent.ShowMessage -> Toast.makeText(this, event.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun showProgress(visibility: Boolean) {
        if (visibility) baseProgressBar.show() else baseProgressBar.hide()
    }
}