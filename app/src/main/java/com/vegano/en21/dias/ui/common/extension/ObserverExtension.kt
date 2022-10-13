package com.vegano.en21.dias.ui.common.extension

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

inline fun <T> Flow<T>.observe(owner: LifecycleOwner, crossinline collector: (T) -> Unit) {
    owner.lifecycleScope.launch {
        owner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            this@observe.collect {
                Log.e("TAG: $owner", "Event: $it")
                collector(it)
            }
        }
    }
}